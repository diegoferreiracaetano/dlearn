package com.diegoferreiracaetano.dlearn.infrastructure.services

import java.util.concurrent.ConcurrentHashMap

class UserDataService {
    // Simulated database: userId -> Set of movieIds
    private val favorites = ConcurrentHashMap<String, MutableSet<String>>()
    private val watchlist = ConcurrentHashMap<String, MutableSet<String>>()

    fun toggleFavorite(userId: String, movieId: String): Boolean {
        val userFavorites = favorites.getOrPut(userId) { mutableSetOf() }
        return if (userFavorites.contains(movieId)) {
            userFavorites.remove(movieId)
            false
        } else {
            userFavorites.add(movieId)
            true
        }
    }

    fun toggleWatchlist(userId: String, movieId: String): Boolean {
        val userWatchlist = watchlist.getOrPut(userId) { mutableSetOf() }
        return if (userWatchlist.contains(movieId)) {
            userWatchlist.remove(movieId)
            false
        } else {
            userWatchlist.add(movieId)
            true
        }
    }

    fun getFavorites(userId: String): Set<String> {
        return favorites[userId] ?: emptySet()
    }

    fun getWatchlist(userId: String): Set<String> {
        return watchlist[userId] ?: emptySet()
    }
}
