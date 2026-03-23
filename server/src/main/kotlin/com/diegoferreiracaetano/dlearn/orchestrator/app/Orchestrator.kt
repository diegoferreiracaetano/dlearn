package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

interface Orchestrator {
    fun execute(
        request: AppRequest,
        userId: String,
        userAgent: AppUserAgent
    ): Flow<Screen>
}
