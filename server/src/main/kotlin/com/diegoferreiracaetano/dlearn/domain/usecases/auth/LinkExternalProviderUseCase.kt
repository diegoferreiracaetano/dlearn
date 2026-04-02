package com.diegoferreiracaetano.dlearn.domain.usecases.auth

import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.infrastructure.auth.AuthProviderSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LinkExternalProviderUseCase(
    private val authProviderSyncService: AuthProviderSyncService,
    private val authProviderRepository: AuthProviderRepository,
) {
    private val scope = CoroutineScope(Dispatchers.IO)

    fun execute(
        userId: String,
        metadata: Map<String, String> = emptyMap(),
    ) {
        scope.launch {
            try {
                // Carrega metadados existentes para não perder credenciais (ex: TMDB username/password)
                // no momento do refresh de token.
                val existingProviders = authProviderRepository.findByUserId(userId)
                val combinedMetadata = mutableMapOf<String, String>()

                // Mescla metadados de todos os provedores existentes
                existingProviders.forEach { provider ->
                    combinedMetadata.putAll(provider.metadata)
                }

                // Sobrescreve com o metadata passado (caso venha da requisição de login/register)
                combinedMetadata.putAll(metadata)

                authProviderSyncService.discoverAndSaveProviders(userId, combinedMetadata)
            } catch (_: Exception) {
                // Silently fail as it is a background task
            }
        }
    }
}
