package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class AppOrchestrator(
    private val mainOrchestrator: MainOrchestrator,
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
            NavigationRoutes.FAVORITE -> {
                val movieId = request.params?.get("movieId")
                if (movieId != null) {
                    favoriteOrchestrator.toggleFavorite(userId, movieId, lang)
                } else {
                    favoriteOrchestrator.getFavorite(userId, lang)
                }
            }
            NavigationRoutes.WATCHLIST -> {
                val movieId = request.params?.get("movieId")
                if (movieId != null) {
                    watchlistOrchestrator.toggleWatchlist(userId, movieId, lang)
                } else {
                    watchlistOrchestrator.getWatchlist(userId, lang)
                }
            }
            else -> {
                mainOrchestrator.getRouteData(
                    userId = userId,
                    appVersion = appVersion,
                    lang = lang,
                    route = request.path,
                    type = HomeFilterType.ALL
                )
            }
        }
    }
}
