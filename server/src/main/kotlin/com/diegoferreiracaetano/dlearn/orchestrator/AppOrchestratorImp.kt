package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.screens.VerifyAccountScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppOrchestratorImpl(
    private val homeOrchestrator: HomeOrchestrator,
    private val favoriteOrchestrator: FavoriteOrchestrator,
    private val watchlistOrchestrator: WatchlistOrchestrator,
    private val profileOrchestrator: ProfileOrchestrator,
    private val faqOrchestrator: FaqOrchestrator,
    private val searchOrchestrator: SearchOrchestrator,
    private val movieDetailOrchestrator: MovieDetailOrchestrator,
    private val mainOrchestrator: MainOrchestrator,
    private val verifyAccountScreenBuilder: VerifyAccountScreenBuilder
) : AppOrchestrator {
    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> {
        val path = NavigationRoutes.extractPath(request.path)
        return when (path) {
            NavigationRoutes.HOME -> homeOrchestrator.handleRequest(request, userId, appVersion, lang)
            NavigationRoutes.FAVORITE -> favoriteOrchestrator.handleRequest(request, userId, lang)
            NavigationRoutes.WATCHLIST -> watchlistOrchestrator.handleRequest(request, userId, lang)
            NavigationRoutes.PROFILE,
            NavigationRoutes.EDIT_PROFILE,
            NavigationRoutes.UPDATE_PROFILE -> profileOrchestrator.handleRequest(request, userId, appVersion, lang)
            NavigationRoutes.FAQ -> faqOrchestrator.handleRequest(request, lang)
            NavigationRoutes.SEARCH -> searchOrchestrator.handleRequest(request, userId, lang)
            NavigationRoutes.MOVIE_DETAIL -> movieDetailOrchestrator.handleRequest(request, appVersion, lang)
            NavigationRoutes.WELCOME -> mainOrchestrator.handleRequest(request, userId, appVersion, lang)
            NavigationRoutes.VERIFY_ACCOUNT -> flow { emit(verifyAccountScreenBuilder.build()) }
            else -> throw IllegalArgumentException("Invalid path: ${request.path} (extracted: $path)")
        }
    }
}
