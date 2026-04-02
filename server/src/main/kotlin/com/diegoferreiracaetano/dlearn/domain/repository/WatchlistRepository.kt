package com.diegoferreiracaetano.dlearn.domain.repository

interface WatchlistRepository {
    suspend fun toggleWatchlist(
        userId: String,
        mediaId: String,
        isInWatchlist: Boolean,
    )

    suspend fun isInWatchlist(
        userId: String,
        mediaId: String,
    ): Boolean

    suspend fun getWatchlist(userId: String): List<String>
}
