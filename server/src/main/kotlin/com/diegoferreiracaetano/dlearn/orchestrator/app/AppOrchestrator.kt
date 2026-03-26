package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAQ
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAVORITE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.HOME
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.MOVIES
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.PROFILE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.PROFILE_EDIT
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.PROFILE_UPDATE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.SEARCH
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.SETTINGS_COUNTRY
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.SETTINGS_LANGUAGE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.SETTINGS_NOTIFICATIONS
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.USERS
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.VERIFY_ACCOUNT
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.WATCHLIST
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.WELCOME
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
        return when (request.path) {
            HOME -> homeOrchestrator.execute(request, header)
            FAVORITE -> favoriteOrchestrator.execute(request, header)
            WATCHLIST -> watchlistOrchestrator.execute(request, header)
            PROFILE, 
            PROFILE_EDIT, 
            PROFILE_UPDATE -> profileOrchestrator.execute(request, header)
            FAQ -> faqOrchestrator.execute(request, header)
            MOVIES -> movieDetailOrchestrator.execute(request, header)
            SEARCH -> searchOrchestrator.execute(request, header)
            WELCOME -> mainOrchestrator.execute(request, header)
            VERIFY_ACCOUNT -> verifyAccountOrchestrator.execute(request, header)
            SETTINGS_NOTIFICATIONS, 
            SETTINGS_LANGUAGE, 
            SETTINGS_COUNTRY -> settingsOrchestrator.execute(request, header)
            USERS -> userListOrchestrator.execute(request, header)
            else -> throw IllegalArgumentException("Invalid path: ${request.path}")
        }
    }
}
