package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import kotlinx.coroutines.flow.Flow

interface WatchlistRepository {
    suspend fun addToWatchlist(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        watchlist: Boolean
    )
    
    fun getWatchlist(userId: String, language: String): Flow<List<Pair<Int, MediaType>>>
}
