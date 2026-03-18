package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class MainOrchestrator(
    private val mainScreenBuilder: MainScreenBuilder
) {
    suspend fun getMainData(
        userId: String,
        appVersion: Int,
        lang: String
    ): Screen {
        return mainScreenBuilder.build(
            userId = userId,
            appVersion = appVersion,
            lang = lang
        )
    }
}
