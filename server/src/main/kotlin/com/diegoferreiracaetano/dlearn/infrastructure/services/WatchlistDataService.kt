package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll

class WatchlistDataService(
    private val tmdbClient: TmdbClient,
    private val authProviderRepository: AuthProviderRepository
) : WatchlistRepository {

    override suspend fun toggleWatchlist(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        isWatchlist: Boolean
    ) {
        val providers = authProviderRepository.findByUserId(userId)
        val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }

        // 1. Sempre persiste no banco local (Source of Truth)
        dbQuery {
            if (isWatchlist) {
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

        // 2. Tenta sincronizar com o TMDB apenas se não for guest e estiver vinculado
        if (tmdbAccount != null) {
            val isGuestSession = tmdbAccount.metadata["auth_type"] == "guest_session"
            if (!isGuestSession) {
                val sessionId = tmdbAccount.metadata[MetadataKeys.TMDB_SESSION_ID] 
                val accountId = tmdbAccount.metadata[MetadataKeys.TMDB_ACCOUNT_ID]
                
                if (sessionId != null && accountId != null) {
                    try {
                        tmdbClient.addToWatchlist(
                            accountId = accountId,
                            sessionId = sessionId,
                            mediaType = mediaType.name.lowercase(),
                            mediaId = mediaId,
                            watchlist = isWatchlist,
                            isGuest = false
                        )
                    } catch (e: Exception) {
                        // Silently fail TMDB sync
                    }
                }
            }
        }
    }

    override suspend fun isWatchlist(userId: String, mediaId: Int, mediaType: MediaType): Boolean = dbQuery {
        WatchlistTable.selectAll().where {
            (WatchlistTable.userId eq userId) and 
            (WatchlistTable.mediaId eq mediaId) and 
            (WatchlistTable.mediaType eq mediaType.name)
        }.count() > 0
    }

    override suspend fun getWatchlist(userId: String): List<Pair<Int, MediaType>> = dbQuery {
        WatchlistTable.selectAll()
            .where { WatchlistTable.userId eq userId }
            .map { 
                it[WatchlistTable.mediaId] to MediaType.valueOf(it[WatchlistTable.mediaType]) 
            }
    }
}
