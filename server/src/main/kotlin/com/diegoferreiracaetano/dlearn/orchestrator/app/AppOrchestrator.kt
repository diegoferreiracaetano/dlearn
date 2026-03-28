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
import com.diegoferreiracaetano.dlearn.navigation.AppPath
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
        val parsedRequest = AppPath.parse(request.path)
        val path = parsedRequest.path
        
        // Merge params from path with original request params
        val mergedParams = (request.params ?: emptyMap()) + (parsedRequest.params ?: emptyMap())
        val finalRequest = request.copy(path = path, params = mergedParams)

        return when (path) {
            HOME -> homeOrchestrator.execute(finalRequest, header)
            FAVORITE, 
            MOVIE_FAVORITE -> favoriteOrchestrator.execute(finalRequest, header)
            WATCHLIST, 
            MOVIE_WATCHLIST -> watchlistOrchestrator.execute(finalRequest, header)
            PROFILE, 
            PROFILE_EDIT, 
            PROFILE_UPDATE -> profileOrchestrator.execute(finalRequest, header)
            FAQ -> faqOrchestrator.execute(finalRequest, header)
            MOVIES -> movieDetailOrchestrator.execute(finalRequest, header)
            SEARCH -> searchOrchestrator.execute(finalRequest, header)
            WELCOME -> mainOrchestrator.execute(finalRequest, header)
            VERIFY_ACCOUNT -> verifyAccountOrchestrator.execute(finalRequest, header)
            SETTINGS_NOTIFICATIONS, 
            SETTINGS_LANGUAGE, 
            SETTINGS_COUNTRY -> settingsOrchestrator.execute(finalRequest, header)
            USERS -> userListOrchestrator.execute(finalRequest, header)
            else -> throw IllegalArgumentException("Invalid path: $path")
        }
    }
}
