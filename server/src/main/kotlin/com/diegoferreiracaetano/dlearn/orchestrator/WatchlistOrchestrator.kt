package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.ui.screens.WatchlistScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.infrastructure.services.UserDataService

class WatchlistOrchestrator(
    private val watchlistScreenBuilder: WatchlistScreenBuilder,
    private val userDataService: UserDataService
) {
    suspend fun getWatchlist(userId: String, lang: String): Screen {
        // In a real app, this would fetch from a database or service
        val watchlistIds = userDataService.getWatchlist(userId)
        return watchlistScreenBuilder.build(lang)
    }

    suspend fun toggleWatchlist(userId: String, movieId: String, lang: String): Screen {
        userDataService.toggleWatchlist(userId, movieId)
        return getWatchlist(userId, lang)
    }
}
