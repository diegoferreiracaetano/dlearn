package com.diegoferreiracaetano.dlearn.domain.app

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow

/**
 * Repository responsible for handling Server-Driven UI requests and actions.
 */
interface AppRepository {
    /**
     * Executes a request to the SDUI gateway.
     *
     * @param path The endpoint or action path.
     * @param params Key-value pairs for request parameters or body.
     * @param metadata Additional information like headers or tracking data.
     * @return A Flow emitting the resulting Screen.
     */
    fun execute(
        path: String,
        params: Map<String, String>? = null,
        metadata: Map<String, String>? = null
    ): Flow<Screen>
}
