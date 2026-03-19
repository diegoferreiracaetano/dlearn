package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import java.util.concurrent.ConcurrentHashMap

class FavoriteDataService : FavoriteRepository {
    private val favorites = ConcurrentHashMap<String, MutableSet<String>>()

    override fun toggleFavorite(userId: String, movieId: String): Boolean {
        val userFavorites = favorites.getOrPut(userId) { mutableSetOf() }
        return if (userFavorites.contains(movieId)) {
            userFavorites.remove(movieId)
            false
        } else {
            userFavorites.add(movieId)
            true
        }
    }

    override fun getFavorites(userId: String): Set<String> {
        return favorites[userId] ?: emptySet()
    }
}
