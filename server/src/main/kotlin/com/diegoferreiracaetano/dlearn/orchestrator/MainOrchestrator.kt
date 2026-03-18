package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class MainOrchestrator(
    private val mainScreenBuilder: MainScreenBuilder,
    private val homeOrchestrator: HomeOrchestrator,
    private val watchlistOrchestrator: WatchlistOrchestrator,
    private val favoriteOrchestrator: FavoriteOrchestrator,
    private val profileOrchestrator: ProfileOrchestrator
) {
    suspend fun getMainData(
        userId: String,
        appVersion: Int,
        lang: String,
    ): Screen {
        return mainScreenBuilder.build(
            userId = userId,
            appVersion = appVersion,
            lang = lang
        )
    }
}
