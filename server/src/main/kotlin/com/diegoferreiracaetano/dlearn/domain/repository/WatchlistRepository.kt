package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.video.MediaType

interface WatchlistRepository {
    suspend fun toggleWatchlist(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        isWatchlist: Boolean
    )
    
    suspend fun isWatchlist(userId: String, mediaId: Int, mediaType: MediaType): Boolean

    suspend fun getWatchlist(userId: String): List<Pair<Int, MediaType>>
}
