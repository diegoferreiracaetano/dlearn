package com.diegoferreiracaetano.dlearn.navigation

import com.diegoferreiracaetano.dlearn.util.encodeURL

sealed class ScreenRouter(
    val route: String,
) {
    object Home : ScreenRouter("Home")
    object New : ScreenRouter("New")
    object Favorites : ScreenRouter("Favorites")
    object Profile : ScreenRouter("Account")
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
