package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.VerifyAccountScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class VerifyAccountOrchestrator(
    private val verifyAccountScreenBuilder: VerifyAccountScreenBuilder,
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String,
    ): Flow<Screen> =
        flow {
            emit(verifyAccountScreenBuilder.build(header.language))
        }
}
