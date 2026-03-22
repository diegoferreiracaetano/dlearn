package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes.EDIT_PROFILE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.FAQ
import com.diegoferreiracaetano.dlearn.NavigationRoutes.FAVORITE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.HOME
import com.diegoferreiracaetano.dlearn.NavigationRoutes.MOVIE_DETAIL
import com.diegoferreiracaetano.dlearn.NavigationRoutes.PROFILE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.SEARCH
import com.diegoferreiracaetano.dlearn.NavigationRoutes.UPDATE_PROFILE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.VERIFY_ACCOUNT
import com.diegoferreiracaetano.dlearn.NavigationRoutes.WATCHLIST
import com.diegoferreiracaetano.dlearn.NavigationRoutes.WELCOME
import com.diegoferreiracaetano.dlearn.NavigationRoutes.extractPath
import com.diegoferreiracaetano.dlearn.ui.screens.VerifyAccountScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class AppOrchestratorImpl(
    private val homeOrchestrator: HomeOrchestratorImpl,
    private val favoriteOrchestrator: FavoriteOrchestratorImpl,
    private val watchlistOrchestrator: WatchlistOrchestratorImpl,
    private val profileOrchestrator: ProfileOrchestratorImpl,
    private val faqOrchestrator: FaqOrchestratorImpl,
    private val searchOrchestrator: SearchOrchestratorImpl,
    private val movieDetailOrchestrator: MovieDetailOrchestratorImpl,
    private val mainOrchestrator: MainOrchestratorImpl,
    private val verifyAccountScreenBuilder: VerifyAccountScreenBuilder
) : AppOrchestrator {
    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> {
        val path = extractPath(request.path)
        return when (path) {
            HOME -> homeOrchestrator.execute(request, userId, lang, appVersion)
            FAVORITE -> favoriteOrchestrator.execute(request, userId, lang, appVersion)
            WATCHLIST -> watchlistOrchestrator.execute(request, userId, lang, appVersion)
            PROFILE, EDIT_PROFILE, UPDATE_PROFILE -> profileOrchestrator.execute(request, userId, lang, appVersion)
            FAQ -> faqOrchestrator.execute(request, userId, lang, appVersion)
            SEARCH -> searchOrchestrator.execute(request, userId, lang, appVersion)
            MOVIE_DETAIL -> movieDetailOrchestrator.execute(request, userId, lang, appVersion)
            WELCOME -> mainOrchestrator.execute(request, userId, lang, appVersion)
            VERIFY_ACCOUNT -> flow { emit(verifyAccountScreenBuilder.build()) }
            else -> throw IllegalArgumentException("Invalid path: ${request.path} (extracted: $path)")
        }
    }
}
