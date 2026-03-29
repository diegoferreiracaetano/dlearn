package com.diegoferreiracaetano.dlearn.infrastructure.auth

import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider
import com.diegoferreiracaetano.dlearn.infrastructure.services.Feature
import com.diegoferreiracaetano.dlearn.infrastructure.services.FeatureToggleService
import org.slf4j.LoggerFactory

class AuthProviderSyncService(
    private val authServices: List<ExternalAuthService>,
    private val featureToggleService: FeatureToggleService,
    private val authProviderRepository: AuthProviderRepository
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun discoverAndSaveProviders(
        userId: String,
        metadata: Map<String, String>
    ) {
        if (!featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC)) {
            logger.info("[Sync] Feature disabled for user $userId")
            return
        }

        logger.info("[Sync] Starting sync for user $userId. Metadata keys: ${metadata.keys}")

        val existingProviders = try {
            authProviderRepository.findByUserId(userId)
        } catch (e: Exception) {
            logger.error("[Sync] Failed to fetch existing providers for $userId", e)
            emptyList()
        }

        val providersToSave = mutableListOf<AuthProvider>()

        authServices.forEach { service ->
            val isAlreadyLinked = existingProviders.any { it.provider == service.provider }
            val canHandle = service.canHandle(metadata)

            logger.info("[Sync] Provider ${service.provider}: alreadyLinked=$isAlreadyLinked, canHandle=$canHandle")

            // Tentamos sincronizar se:
            // 1. Não está vinculado E o serviço consegue lidar com os metadados atuais.
            // 2. OU se o front enviou metadados explicitamente (indica uma tentativa de (re)vínculo).
            if (canHandle) {
                try {
                    logger.info("[Sync] Authenticating with ${service.provider} for user $userId...")
                    val authData = service.authenticate(metadata)
                    
                    if (authData.isNotEmpty()) {
                        val externalId = authData["external_id"] ?: ""
                        val providerInfo = AuthProvider(
                            provider = service.provider,
                            externalId = externalId,
                            metadata = authData
                        )
                        providersToSave.add(providerInfo)
                        logger.info("[Sync] ${service.provider} authenticated successfully. ExternalId: $externalId")
                    } else {
                        logger.warn("[Sync] ${service.provider} returned empty authentication data")
                    }
                } catch (e: Exception) {
                    logger.error("[Sync] Error during authentication with ${service.provider}", e)
                }
            } else {
                if (!isAlreadyLinked) {
                    logger.info("[Sync] Skipping ${service.provider}: No credentials provided and not yet linked.")
                }
            }
        }
        
        if (providersToSave.isNotEmpty()) {
            try {
                logger.info("[Sync] Saving ${providersToSave.size} providers to DB...")
                authProviderRepository.saveAll(userId, providersToSave)
                logger.info("[Sync] Successfully persisted providers for user $userId")
            } catch (e: Exception) {
                logger.error("[Sync] Failed to save providers to repository", e)
            }
        } else {
            logger.info("[Sync] No providers were identified for saving.")
        }
    }
}
