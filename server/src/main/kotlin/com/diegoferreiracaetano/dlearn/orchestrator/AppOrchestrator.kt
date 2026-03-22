package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.screens.VerifyAccountScreenBuilder
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppOrchestrator(
    private val homeOrchestrator: HomeOrchestrator,
    private val favoriteOrchestrator: FavoriteOrchestrator,
    private val watchlistOrchestrator: WatchlistOrchestrator,
    private val profileOrchestrator: ProfileOrchestrator,
    private val faqOrchestrator: FaqOrchestrator,
    private val verifyAccountScreenBuilder: VerifyAccountScreenBuilder
) {
    fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> {
        val path = NavigationRoutes.extractPath(request.path)
        return when (path) {
            NavigationRoutes.HOME -> homeOrchestrator.getHomeData(userId, appVersion, lang)
            NavigationRoutes.FAVORITE -> handleFavoriteRequest(request, userId, lang)
            NavigationRoutes.WATCHLIST -> handleWatchlistRequest(request, userId, lang)
            NavigationRoutes.PROFILE -> profileOrchestrator.getProfileData(userId, appVersion, lang)
            NavigationRoutes.EDIT_PROFILE -> profileOrchestrator.getEditProfileData(userId, lang)
            NavigationRoutes.UPDATE_PROFILE -> profileOrchestrator.updateProfile(userId, request.params ?: emptyMap(), lang)
            NavigationRoutes.VERIFY_ACCOUNT -> flow { emit(verifyAccountScreenBuilder.build()) }
            NavigationRoutes.FAQ -> handleFaqRequest(request)
            else -> throw IllegalArgumentException("Invalid path: ${request.path} (extracted: $path)")
        }
    }

    private fun handleFaqRequest(request: AppRequest): Flow<Screen> {
        val reference = request.params?.get(NavigationRoutes.FAQ_REF_ARG)
            ?: throw IllegalArgumentException("FAQ reference missing")
        return faqOrchestrator.getFaqData(reference)
    }

    private fun handleFavoriteRequest(request: AppRequest, userId: String, lang: String): Flow<Screen> {
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
        return if (movieId != null) {
            favoriteOrchestrator.toggleFavorite(userId, movieId, lang)
        } else {
            favoriteOrchestrator.getFavorite(userId, lang)
        }
    }

    private fun handleWatchlistRequest(request: AppRequest, userId: String, lang: String): Flow<Screen> {
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
        return if (movieId != null) {
            watchlistOrchestrator.toggleWatchlist(userId, movieId, lang)
        } else {
            watchlistOrchestrator.getWatchlist(userId, lang)
        }
    }
}
