package com.diegoferreiracaetano.dlearn.domain.usecases.auth

import com.diegoferreiracaetano.dlearn.infrastructure.auth.AuthProviderSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LinkExternalProviderUseCase(
    private val authProviderSyncService: AuthProviderSyncService
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun execute(userId: String, metadata: Map<String, String> = emptyMap()) {
        scope.launch {
            try {
                authProviderSyncService.discoverAndSaveProviders(userId, metadata)
            } catch (_: Exception) {
            }
        }
    }
}
