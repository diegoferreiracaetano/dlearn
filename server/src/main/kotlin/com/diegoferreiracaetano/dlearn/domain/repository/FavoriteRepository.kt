package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.video.MediaType

interface FavoriteRepository {
    suspend fun toggleFavorite(userId: String, mediaId: Int, mediaType: MediaType, isFavorite: Boolean)
    suspend fun isFavorite(userId: String, mediaId: Int, mediaType: MediaType): Boolean
    suspend fun getFavorites(userId: String): List<Pair<Int, MediaType>>
}
