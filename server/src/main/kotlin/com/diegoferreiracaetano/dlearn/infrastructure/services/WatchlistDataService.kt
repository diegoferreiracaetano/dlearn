package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll

class WatchlistDataService(
    private val tmdbClient: TmdbClient,
    private val authProviderRepository: AuthProviderRepository
) : WatchlistRepository {

    override suspend fun addToWatchlist(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        watchlist: Boolean
    ) {
        val providers = authProviderRepository.findByUserId(userId)
        val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
            ?: throw IllegalStateException("TMDB_NOT_LINKED")

        val sessionId = tmdbAccount.metadata[MetadataKeys.TMDB_SESSION_ID] 
            ?: throw IllegalStateException("TMDB_SESSION_MISSING")
        
        val isGuestSession = tmdbAccount.metadata["auth_type"] == "guest_session"
        val accountId = tmdbAccount.metadata[MetadataKeys.TMDB_ACCOUNT_ID] ?: if (isGuestSession) "me" else throw IllegalStateException("TMDB_ACCOUNT_ID_MISSING")

        // 1. Sempre persiste no banco local (Source of Truth para Guest)
        dbQuery {
            if (watchlist) {
                WatchlistTable.insertIgnore {
                    it[WatchlistTable.userId] = userId
                    it[WatchlistTable.mediaId] = mediaId
                    it[WatchlistTable.mediaType] = mediaType.name
                }
            } else {
                WatchlistTable.deleteWhere {
                    (WatchlistTable.userId eq userId) and 
                    (WatchlistTable.mediaId eq mediaId) and 
                    (WatchlistTable.mediaType eq mediaType.name)
                }
            }
        }

        // 2. Tenta sincronizar com o TMDB apenas se não for guest
        if (!isGuestSession) {
            try {
                tmdbClient.addToWatchlist(
                    accountId = accountId,
                    sessionId = sessionId,
                    mediaType = mediaType.name.lowercase(),
                    mediaId = mediaId,
                    watchlist = watchlist,
                    isGuest = false
                )
            } catch (e: Exception) {
                // Silently fail TMDB sync
            }
        }
    }

    override fun getWatchlist(
        userId: String,
        language: String
    ): Flow<List<Pair<Int, MediaType>>> = flow {
        // Source of truth: Local Database
        val localWatchlist = dbQuery {
            WatchlistTable.selectAll()
                .where { WatchlistTable.userId eq userId }
                .map { 
                    it[WatchlistTable.mediaId] to MediaType.valueOf(it[WatchlistTable.mediaType]) 
                }
        }
        
        emit(localWatchlist)
    }
}
