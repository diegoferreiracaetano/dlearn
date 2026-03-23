package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.AppRequestContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MainOrchestrator(
    private val mainScreenBuilder: MainScreenBuilder
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        userId: String,
        userAgent: String
    ): Flow<Screen> = flow {
        val context = AppRequestContext.fromUserAgent(userAgent)
        emit(
            mainScreenBuilder.build(
                userId = userId,
                appVersion = context.appVersion,
                lang = context.lang
            )
        )
    }
}
