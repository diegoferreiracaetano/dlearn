package com.diegoferreiracaetano.dlearn.domain.repository

interface WatchlistRepository {
    fun toggleWatchlist(userId: String, movieId: String): Boolean
    fun getWatchlist(userId: String): Set<String>
}
