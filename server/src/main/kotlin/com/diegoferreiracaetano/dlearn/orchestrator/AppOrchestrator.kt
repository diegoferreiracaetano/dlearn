package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class AppOrchestrator(
    private val favoriteOrchestrator: FavoriteOrchestrator,
    private val watchlistOrchestrator: WatchlistOrchestrator,
) {
    suspend fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Screen {
        return when (request.path) {
            NavigationRoutes.FAVORITE -> handleFavoriteRequest(request, userId, lang)
            NavigationRoutes.WATCHLIST -> handleWatchlistRequest(request, userId, lang)
            else ->  throw IllegalArgumentException("Invalid path: ${request.path}")
        }
    }

    private suspend fun handleFavoriteRequest(request: AppRequest, userId: String, lang: String): Screen {
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
        return if (movieId != null) {
            favoriteOrchestrator.toggleFavorite(userId, movieId, lang)
        } else {
            favoriteOrchestrator.getFavorite(userId, lang)
        }
    }

    private suspend fun handleWatchlistRequest(request: AppRequest, userId: String, lang: String): Screen {
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
        return if (movieId != null) {
            watchlistOrchestrator.toggleWatchlist(userId, movieId, lang)
        } else {
            watchlistOrchestrator.getWatchlist(userId, lang)
        }
    }
}
