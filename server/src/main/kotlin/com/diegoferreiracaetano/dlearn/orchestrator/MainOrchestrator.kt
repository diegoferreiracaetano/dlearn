package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.GetProfileDataUseCase
import com.diegoferreiracaetano.dlearn.ui.screens.*
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.NavigationRoutes

class MainOrchestrator(
    private val mainScreenBuilder: MainScreenBuilder,
    private val homeScreenBuilder: HomeScreenBuilder,
    private val newScreenBuilder: NewScreenBuilder,
    private val favoriteScreenBuilder: FavoriteScreenBuilder,
    private val profileScreenBuilder: ProfileScreenBuilder,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val getProfileDataUseCase: GetProfileDataUseCase
) {
    suspend fun getMainData(
        userId: String,
        appVersion: Int,
        lang: String,
        route: String,
        type: HomeFilterType
    ): Screen {
        return mainScreenBuilder.build(
            userId = userId,
            appVersion = appVersion,
            lang = lang,
            route = route,
            type = type
        )
    }

    suspend fun getRouteData(
        userId: String,
        appVersion: Int,
        lang: String,
        route: String,
        type: HomeFilterType
    ): Screen {
        return when (route) {
            NavigationRoutes.HOME -> {
                val domainData = getHomeDataUseCase.execute(userId, type)
                homeScreenBuilder.build(domainData, appVersion, lang, type)
            }
            NavigationRoutes.NEW -> {
                val domainData = getHomeDataUseCase.execute(userId, type)
                newScreenBuilder.build(domainData, lang)
            }
            NavigationRoutes.FAVORITE -> {
                val domainData = getHomeDataUseCase.execute(userId, type)
                favoriteScreenBuilder.build(domainData, lang)
            }
            NavigationRoutes.PROFILE -> {
                val profileData = getProfileDataUseCase.execute(lang)
                profileScreenBuilder.build(profileData, appVersion, lang)
            }
            else -> {
                val domainData = getHomeDataUseCase.execute(userId, type)
                homeScreenBuilder.build(domainData, appVersion, lang, type)
            }
        }
    }
}
