package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface Orchestrator {
    fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String
    ): Flow<Screen>
}
