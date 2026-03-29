package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.MetadataKeys.TMDB_ACCOUNT_ID
import com.diegoferreiracaetano.dlearn.MetadataKeys.TMDB_SESSION_ID
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll

class FavoriteDataService(
    private val tmdbClient: TmdbClient,
    private val authProviderRepository: AuthProviderRepository
) : FavoriteRepository {

    override suspend fun markAsFavorite(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        favorite: Boolean
    ) {
        val providers = authProviderRepository.findByUserId(userId)
        val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
            ?: throw IllegalStateException("TMDB_NOT_LINKED")

        val sessionId = tmdbAccount.metadata[TMDB_SESSION_ID]
            ?: throw IllegalStateException("TMDB_SESSION_MISSING")
        
        val isGuestSession = tmdbAccount.metadata["auth_type"] == "guest_session"
        val accountId = tmdbAccount.metadata[TMDB_ACCOUNT_ID] ?: if (isGuestSession) "me" else throw IllegalStateException("TMDB_ACCOUNT_ID_MISSING")

        // 1. Sempre persiste no banco local (Source of Truth para Guest)
        dbQuery {
            if (favorite) {
                FavoriteTable.insertIgnore {
                    it[FavoriteTable.userId] = userId
                    it[FavoriteTable.mediaId] = mediaId
                    it[FavoriteTable.mediaType] = mediaType.name
                }
            } else {
                FavoriteTable.deleteWhere {
                    (FavoriteTable.userId eq userId) and 
                    (FavoriteTable.mediaId eq mediaId) and 
                    (FavoriteTable.mediaType eq mediaType.name)
                }
            }
        }

        // 2. Tenta sincronizar com o TMDB apenas se não for guest (pois TMDB Guest não suporta favoritos)
        if (!isGuestSession) {
            try {
                tmdbClient.markAsFavorite(
                    accountId = accountId,
                    sessionId = sessionId,
                    mediaType = mediaType.name.lowercase(),
                    mediaId = mediaId,
                    favorite = favorite,
                    isGuest = false
                )
            } catch (e: Exception) {
                // Silently fail TMDB sync
            }
        }
    }

    override fun getFavorites(
        userId: String,
        language: String
    ): Flow<List<Pair<Int, MediaType>>> = flow {
        // Source of truth: Local Database
        val localFavorites = dbQuery {
            FavoriteTable.selectAll()
                .where { FavoriteTable.userId eq userId }
                .map { 
                    it[FavoriteTable.mediaId] to MediaType.valueOf(it[FavoriteTable.mediaType]) 
                }
        }
        
        emit(localFavorites)
    }
}
