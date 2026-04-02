package com.diegoferreiracaetano.dlearn.navigation

sealed class ScreenRouter(
    val route: String,
) {
    object Home : ScreenRouter(AppNavigationRoute.HOME)

    object Profile : ScreenRouter(AppNavigationRoute.PROFILE)

    object Watchlist : ScreenRouter(AppNavigationRoute.WATCHLIST)

    object Favorite : ScreenRouter(AppNavigationRoute.FAVORITE)

    object Search : ScreenRouter(AppNavigationRoute.SEARCH)

    object Welcome : ScreenRouter(AppNavigationRoute.WELCOME)

    object Login : ScreenRouter(AppNavigationRoute.LOGIN)

    object Logout : ScreenRouter(AppNavigationRoute.LOGOUT)

    object SignUp : ScreenRouter(AppNavigationRoute.SIGNUP)

    object ResetPassword : ScreenRouter(AppNavigationRoute.RESET_PASSWORD)

    object VerifyAccount : ScreenRouter(AppNavigationRoute.VERIFY_ACCOUNT)

    object CreateNewPassword : ScreenRouter(AppNavigationRoute.CREATE_NEW_PASSWORD)

    object ChangePassword : ScreenRouter(AppNavigationRoute.PROFILE_CHANGE_PASSWORD)

    object Onboarding : ScreenRouter(AppNavigationRoute.ONBOARDING)

    object SettingsNotifications : ScreenRouter(AppNavigationRoute.SETTINGS_NOTIFICATIONS)

    object SettingsLanguage : ScreenRouter(AppNavigationRoute.SETTINGS_LANGUAGE)

    object SettingsCountry : ScreenRouter(AppNavigationRoute.SETTINGS_COUNTRY)

    object SettingsClearCache : ScreenRouter(AppNavigationRoute.SETTINGS_CLEAR_CACHE)

    object MovieDetail : ScreenRouter(AppNavigationRoute.MOVIE_DETAIL_ROUTE) {
        fun createRoute(movieId: String) = "${AppNavigationRoute.MOVIES}/$movieId"
    }
}
