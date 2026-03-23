package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainOrchestrator(
    private val mainScreenBuilder: MainScreenBuilder
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> = flow {
        emit(
            mainScreenBuilder.build(
                lang = header.language
            )
        )
    }
}
