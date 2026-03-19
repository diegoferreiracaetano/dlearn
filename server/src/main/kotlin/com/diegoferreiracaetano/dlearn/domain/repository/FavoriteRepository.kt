package com.diegoferreiracaetano.dlearn.domain.repository

interface FavoriteRepository {
    fun toggleFavorite(userId: String, movieId: String): Boolean
    fun getFavorites(userId: String): Set<String>
}
