package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainOrchestrator(
    private val mainScreenBuilder: MainScreenBuilder
) {
    fun getMainData(
        userId: String,
        appVersion: Int,
        lang: String
    ): Flow<Screen> = flow {
        emit(
            mainScreenBuilder.build(
                userId = userId,
                appVersion = appVersion,
                lang = lang
            )
        )
    }
}
