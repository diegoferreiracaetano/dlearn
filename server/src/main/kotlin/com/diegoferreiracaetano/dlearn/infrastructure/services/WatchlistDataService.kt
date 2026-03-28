package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient

class WatchlistDataService(
    private val tmdbClient: TmdbClient
) : WatchlistRepository {

    override suspend fun addToWatchlist(
        accountId: String,
        sessionId: String,
        mediaId: Int,
        watchlist: Boolean
    ): Boolean {
        return try {
            tmdbClient.addToWatchlist(
                accountId = accountId,
                sessionId = sessionId,
                mediaType = "movie",
                mediaId = mediaId,
                watchlist = watchlist
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getWatchlist(accountId: String, sessionId: String, language: String): List<Int> {
        return try {
            tmdbClient.getWatchlistMovies(accountId, sessionId, language).results.map { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
