package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import java.util.concurrent.ConcurrentHashMap

class WatchlistDataService : WatchlistRepository {
    private val watchlist = ConcurrentHashMap<String, MutableSet<String>>()

    override fun toggleWatchlist(userId: String, movieId: String): Boolean {
        val userWatchlist = watchlist.getOrPut(userId) { mutableSetOf() }
        return if (userWatchlist.contains(movieId)) {
            userWatchlist.remove(movieId)
            false
        } else {
            userWatchlist.add(movieId)
            true
        }
    }

    override fun getWatchlist(userId: String): Set<String> {
        return watchlist[userId] ?: emptySet()
    }
}
