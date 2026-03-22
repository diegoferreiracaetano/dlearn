package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainOrchestratorImpl(
    private val mainScreenBuilder: MainScreenBuilder
) : AppOrchestrator {
    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
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
