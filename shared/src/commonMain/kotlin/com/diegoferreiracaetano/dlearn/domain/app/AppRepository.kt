package com.diegoferreiracaetano.dlearn.domain.app

import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for handling Server-Driven UI requests and actions.
 */
interface AppRepository {
    /**
     * Executes a request to the SDUI gateway.
     *
     * @param request The app request containing path, params and metadata.
     * @return A Flow emitting the resulting Screen.
     */
    fun execute(request: AppRequest): Flow<Screen>
}
