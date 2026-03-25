package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes.EDIT_PROFILE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.FAQ
import com.diegoferreiracaetano.dlearn.NavigationRoutes.FAVORITE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.HOME
import com.diegoferreiracaetano.dlearn.NavigationRoutes.MOVIE_DETAIL
import com.diegoferreiracaetano.dlearn.NavigationRoutes.PROFILE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.SETTINGS_COUNTRY
import com.diegoferreiracaetano.dlearn.NavigationRoutes.SETTINGS_LANGUAGE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.SETTINGS_NOTIFICATIONS
import com.diegoferreiracaetano.dlearn.NavigationRoutes.UPDATE_PROFILE
import com.diegoferreiracaetano.dlearn.NavigationRoutes.USER_LIST
import com.diegoferreiracaetano.dlearn.NavigationRoutes.VERIFY_ACCOUNT
import com.diegoferreiracaetano.dlearn.NavigationRoutes.WATCHLIST
import com.diegoferreiracaetano.dlearn.NavigationRoutes.WELCOME
import com.diegoferreiracaetano.dlearn.NavigationRoutes.extractPath
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

class AppOrchestrator(
    private val homeOrchestrator: HomeOrchestrator,
    private val favoriteOrchestrator: FavoriteOrchestrator,
    private val watchlistOrchestrator: WatchlistOrchestrator,
    private val profileOrchestrator: ProfileOrchestrator,
    private val faqOrchestrator: FaqOrchestrator,
    private val searchOrchestrator: SearchOrchestrator,
    private val movieDetailOrchestrator: MovieDetailOrchestrator,
    private val mainOrchestrator: MainOrchestrator,
    private val verifyAccountOrchestrator: VerifyAccountOrchestrator,
    private val settingsOrchestrator: SettingsOrchestrator,
    private val userListOrchestrator: UserListOrchestrator
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val path = extractPath(request.path)
        return when (path) {
            HOME -> homeOrchestrator.execute(request, header)
            FAVORITE -> favoriteOrchestrator.execute(request, header)
            WATCHLIST -> watchlistOrchestrator.execute(request, header)
            PROFILE, EDIT_PROFILE, UPDATE_PROFILE -> profileOrchestrator.execute(request, header)
            FAQ -> faqOrchestrator.execute(request, header)
            MOVIE_DETAIL -> movieDetailOrchestrator.execute(request, header)
            WELCOME -> mainOrchestrator.execute(request, header)
            VERIFY_ACCOUNT -> verifyAccountOrchestrator.execute(request, header)
            SETTINGS_NOTIFICATIONS, SETTINGS_LANGUAGE, SETTINGS_COUNTRY -> settingsOrchestrator.execute(request, header)
            USER_LIST -> userListOrchestrator.execute(request, header)
            else -> throw IllegalArgumentException("Invalid path: ${request.path} (extracted: $path)")
        }
    }
}
