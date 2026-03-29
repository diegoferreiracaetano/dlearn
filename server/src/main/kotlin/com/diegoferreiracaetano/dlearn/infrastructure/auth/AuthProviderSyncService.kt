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

    suspend fun syncExistingProviders(
        userId: String,
        metadata: Map<String, String>
    ) {
        if (!featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC)) return

        val currentProviders = authProviderRepository.findByUserId(userId)
        val updatedProviders = currentProviders.map { provider ->
            val authService = authServices.find { it.provider == provider.provider }
            if (authService != null && authService.canHandle(provider.metadata + metadata)) {
                try {
                    val authData = authService.authenticate(provider.metadata + metadata)
                    if (authData.isNotEmpty()) {
                        provider.copy(metadata = provider.metadata + authData)
                    } else provider
                } catch (e: Exception) {
                    logger.error("Failed to sync provider ${provider.provider} for user $userId", e)
                    provider
                }
            } else provider
        }

        if (updatedProviders != currentProviders) {
            authProviderRepository.deleteByUserId(userId)
            authProviderRepository.saveAll(userId, updatedProviders)
        }
    }

    suspend fun discoverAndSaveProviders(
        userId: String,
        metadata: Map<String, String>
    ) {
        if (!featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC)) {
            logger.info("External auth sync is disabled by feature toggle")
            return
        }

        logger.info("Discovering providers for user $userId with metadata keys: ${metadata.keys}")

        val providers = mutableListOf<AuthProvider>()
        authServices.forEach { service ->
            if (service.canHandle(metadata)) {
                logger.info("Service ${service.provider} can handle the provided metadata")
                try {
                    val authData = service.authenticate(metadata)
                    if (authData.isNotEmpty()) {
                        val externalId = authData["external_id"] ?: ""
                        providers.add(
                            AuthProvider(
                                provider = service.provider,
                                externalId = externalId,
                                metadata = authData
                            )
                        )
                        logger.info("Successfully authenticated with ${service.provider}. External ID: $externalId")
                    } else {
                        logger.warn("Service ${service.provider} returned empty auth data")
                    }
                } catch (e: Exception) {
                    logger.error("Error authenticating with ${service.provider} during discovery", e)
                }
            }
        }
        
        if (providers.isNotEmpty()) {
            logger.info("Saving ${providers.size} new providers for user $userId")
            authProviderRepository.saveAll(userId, providers)
        } else {
            logger.info("No providers discovered for user $userId")
        }
    }
}
