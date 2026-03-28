package com.diegoferreiracaetano.dlearn.domain.repository

interface FavoriteRepository {
    suspend fun markAsFavorite(
        accountId: String,
        sessionId: String,
        mediaId: Int,
        favorite: Boolean
    ): Boolean
    
    suspend fun getFavorites(accountId: String, sessionId: String, language: String): List<Int>
}
