package com.diegoferreiracaetano.dlearn.navigation

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import io.ktor.http.encodeURLParameter

sealed class ScreenRouter(
    val route: String,
) {
    object Home : ScreenRouter(NavigationRoutes.HOME)
    object Profile : ScreenRouter(NavigationRoutes.PROFILE)
    object Watchlist : ScreenRouter(NavigationRoutes.WATCHLIST)
    object Favorite : ScreenRouter(NavigationRoutes.FAVORITE)

    object Search : ScreenRouter(NavigationRoutes.SEARCH)
    object Welcome : ScreenRouter(NavigationRoutes.WELCOME)
    object Login : ScreenRouter(NavigationRoutes.LOGIN)
    object SignUp : ScreenRouter(NavigationRoutes.SIGNUP)
    object ResetPassword : ScreenRouter(NavigationRoutes.RESET_PASSWORD)
    object VerifyAccount : ScreenRouter(NavigationRoutes.VERIFY_ACCOUNT)
    object CreateNewPassword : ScreenRouter(NavigationRoutes.CREATE_NEW_PASSWORD)
    object ChangePassword : ScreenRouter(NavigationRoutes.CHANGE_PASSWORD)
    object Onboarding : ScreenRouter(NavigationRoutes.ONBOARDING)

    object SettingsNotifications : ScreenRouter(NavigationRoutes.SETTINGS_NOTIFICATIONS)
    object SettingsLanguage : ScreenRouter(NavigationRoutes.SETTINGS_LANGUAGE)
    object SettingsCountry : ScreenRouter(NavigationRoutes.SETTINGS_COUNTRY)

    object MovieDetail : ScreenRouter(NavigationRoutes.MOVIE_DETAIL_ROUTE) {
        fun createRoute(movieId: String) = "${NavigationRoutes.MOVIE_DETAIL}/${movieId.encodeURLParameter()}"
    }
}
