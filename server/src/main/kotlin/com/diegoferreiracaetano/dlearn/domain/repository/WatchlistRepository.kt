package com.diegoferreiracaetano.dlearn.domain.repository

interface WatchlistRepository {
    suspend fun addToWatchlist(
        accountId: String,
        sessionId: String,
        mediaId: Int,
        watchlist: Boolean
    ): Boolean
    
    suspend fun getWatchlist(accountId: String, sessionId: String, language: String): List<Int>
}
