package com.diegoferreiracaetano.dlearn.navigation

sealed class ScreenRouter(
    val route: String,
) {
    object Home : ScreenRouter("Home")
    object New : ScreenRouter("New")
    object Favorites : ScreenRouter("Favorites")
    object Account : ScreenRouter("Account")
    object Onboarding : ScreenRouter("onboarding")
    object Regions : ScreenRouter("regions")
}
