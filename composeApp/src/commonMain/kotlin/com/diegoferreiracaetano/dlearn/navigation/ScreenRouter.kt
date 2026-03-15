package com.diegoferreiracaetano.dlearn.navigation

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.util.encodeURL

sealed class ScreenRouter(
    val route: String,
) {
    object Home : ScreenRouter(NavigationRoutes.HOME)
    object New : ScreenRouter(NavigationRoutes.NEW)
    object Favorites : ScreenRouter(NavigationRoutes.FAVORITE)
    object Profile : ScreenRouter(NavigationRoutes.PROFILE)

    object Search : ScreenRouter("search")
    object Welcome : ScreenRouter("welcome")
    object Login : ScreenRouter("login")
    object SignUp : ScreenRouter("signup")
    object ResetPassword : ScreenRouter("reset_password")
    object VerifyAccount : ScreenRouter("verify_account")
    object CreateNewPassword : ScreenRouter("create_new_password")
    object Regions : ScreenRouter("regions")
    object Onboarding : ScreenRouter("onboarding1")
    object MovieDetail : ScreenRouter("movie_detail/{movieId}") {
        fun createRoute(movieId: String) = "movie_detail/${movieId.encodeURL()}"
    }
}
