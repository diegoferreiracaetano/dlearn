package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class AppOrchestrator(
    private val homeOrchestrator: HomeOrchestrator,
    private val favoriteOrchestrator: FavoriteOrchestrator,
    private val watchlistOrchestrator: WatchlistOrchestrator,
    private val profileOrchestrator: ProfileOrchestrator,
) {
    suspend fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Screen {
        val path = NavigationRoutes.extractPath(request.path)
        return when (path) {
            NavigationRoutes.HOME -> homeOrchestrator.getHomeData(userId, appVersion, lang)
            NavigationRoutes.FAVORITE -> handleFavoriteRequest(request, userId, lang)
            NavigationRoutes.WATCHLIST -> handleWatchlistRequest(request, userId, lang)
            NavigationRoutes.PROFILE -> profileOrchestrator.getProfileData(userId, appVersion, lang)
            NavigationRoutes.EDIT_PROFILE -> profileOrchestrator.getEditProfileData(userId, lang)
            NavigationRoutes.UPDATE_PROFILE -> profileOrchestrator.updateProfile(userId, request.params  ?: emptyMap(), lang)
            else ->  throw IllegalArgumentException("Invalid path: ${request.path} (extracted: $path)")
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
