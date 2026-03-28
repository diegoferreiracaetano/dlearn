package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import kotlinx.coroutines.flow.Flow

interface FavoriteRepository {
    suspend fun markAsFavorite(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        favorite: Boolean
    )

    fun getFavorites(userId: String, language: String): Flow<List<Pair<Int, MediaType>>>
}
