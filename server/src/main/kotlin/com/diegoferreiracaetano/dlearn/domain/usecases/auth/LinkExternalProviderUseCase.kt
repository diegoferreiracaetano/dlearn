package com.diegoferreiracaetano.dlearn.domain.usecases.auth

import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.infrastructure.auth.AuthProviderSyncService
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LinkExternalProviderUseCase(
    private val authProviderSyncService: AuthProviderSyncService,
    private val authProviderRepository: AuthProviderRepository,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO,
) {
    private val scope = CoroutineScope(ioDispatcher)

    fun execute(
        userId: String,
        metadata: Map<String, String> = emptyMap(),
    ) {
        scope.launch {
            try {
                val existingProviders = authProviderRepository.findByUserId(userId)
                val combinedMetadata = mutableMapOf<String, String>()

                existingProviders.forEach { provider ->
                    combinedMetadata.putAll(provider.metadata)
                }

                combinedMetadata.putAll(metadata)

                authProviderSyncService.discoverAndSaveProviders(userId, combinedMetadata)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }
}
