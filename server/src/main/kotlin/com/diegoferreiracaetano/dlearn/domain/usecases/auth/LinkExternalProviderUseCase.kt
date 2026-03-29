package com.diegoferreiracaetano.dlearn.domain.usecases.auth

import com.diegoferreiracaetano.dlearn.infrastructure.auth.AuthProviderSyncService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.slf4j.LoggerFactory

/**
 * Caso de uso isolado para vincular provedores externos (como TMDB).
 * Este fluxo é disparado de forma independente do login/cadastro.
 */
class LinkExternalProviderUseCase(
    private val authProviderSyncService: AuthProviderSyncService
) {
    private val logger = LoggerFactory.getLogger(javaClass)
    private val scope = CoroutineScope(Dispatchers.IO)

    fun execute(userId: String) {
        // Executamos em background para não bloquear o fluxo de autenticação principal
        scope.launch {
            try {
                logger.info("Starting background external provider link for user: $userId")
                authProviderSyncService.discoverAndSaveProviders(userId, emptyMap())
                logger.info("External provider link process finished for user: $userId")
            } catch (e: Exception) {
                logger.error("Failed to link external provider for user: $userId", e)
            }
        }
    }
}
