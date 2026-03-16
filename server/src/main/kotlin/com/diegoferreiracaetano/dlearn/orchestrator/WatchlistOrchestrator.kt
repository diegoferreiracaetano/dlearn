package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.ui.screens.WatchlistScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class WatchlistOrchestrator(
    private val watchlistScreenBuilder: WatchlistScreenBuilder
) {
    suspend fun getWatchlist(userId: String, lang: String): Screen {
        // In a real app, this would fetch from a database or service
        return watchlistScreenBuilder.build(lang)
    }

    suspend fun toggleWatchlist(userId: String, movieId: String, lang: String): Screen {
        // Logic to add/remove from watchlist
        return watchlistScreenBuilder.build(lang)
    }
}
