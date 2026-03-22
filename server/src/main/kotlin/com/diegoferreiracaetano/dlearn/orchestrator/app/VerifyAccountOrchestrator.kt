package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.ui.screens.VerifyAccountScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VerifyAccountOrchestrator(
    private val verifyAccountScreenBuilder: VerifyAccountScreenBuilder
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> = flow {
        emit(verifyAccountScreenBuilder.build())
    }
}
