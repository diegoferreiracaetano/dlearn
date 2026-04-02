package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAQ
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAVORITE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.HOME
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.MOVIES
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.MOVIE_FAVORITE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.MOVIE_WATCHLIST
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
    private val orchestrators: Map<String, Orchestrator>,
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String,
    ): Flow<Screen> {
        val path = request.path

        val orchestrator =
            when (path) {
                HOME -> orchestrators[HOME]
                FAVORITE, MOVIE_FAVORITE -> orchestrators[FAVORITE]
                WATCHLIST, MOVIE_WATCHLIST -> orchestrators[WATCHLIST]
                PROFILE, PROFILE_EDIT, PROFILE_UPDATE -> orchestrators[PROFILE]
                FAQ -> orchestrators[FAQ]
                MOVIES -> orchestrators[MOVIES]
                SEARCH -> orchestrators[SEARCH]
                WELCOME -> orchestrators[WELCOME]
                VERIFY_ACCOUNT -> orchestrators[VERIFY_ACCOUNT]
                SETTINGS_NOTIFICATIONS, SETTINGS_LANGUAGE, SETTINGS_COUNTRY -> orchestrators[SETTINGS_NOTIFICATIONS]
                USERS -> orchestrators[USERS]
                else -> null
            } ?: throw IllegalArgumentException("Invalid path: $path")

        return orchestrator.execute(request, header, userId)
    }
}
