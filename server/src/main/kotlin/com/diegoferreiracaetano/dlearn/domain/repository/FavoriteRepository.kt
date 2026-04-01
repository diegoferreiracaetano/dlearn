package com.diegoferreiracaetano.dlearn.domain.repository

interface FavoriteRepository {
    suspend fun toggleFavorite(userId: String, mediaId: String, isFavorite: Boolean)
    suspend fun isFavorite(userId: String, mediaId: String): Boolean
    suspend fun getFavorites(userId: String): List<String>
}
