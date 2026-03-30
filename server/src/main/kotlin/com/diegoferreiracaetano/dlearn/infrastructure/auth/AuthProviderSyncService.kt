package com.diegoferreiracaetano.dlearn.infrastructure.auth

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider
import com.diegoferreiracaetano.dlearn.infrastructure.services.Feature
import com.diegoferreiracaetano.dlearn.infrastructure.services.FeatureToggleService

class AuthProviderSyncService(
    private val authServices: List<ExternalAuthService>,
    private val featureToggleService: FeatureToggleService,
    private val authProviderRepository: AuthProviderRepository
) {
    suspend fun discoverAndSaveProviders(
        userId: String,
        metadata: Map<String, String>
    ) {
        if (!featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC)) {
            return
        }

        val providersToSave = mutableListOf<AuthProvider>()

        authServices.forEach { service ->
            if (service.canHandle(metadata)) {
                try {
                    val authData = service.authenticate(metadata)
                    
                    if (authData.isNotEmpty()) {
                        val externalId = authData[MetadataKeys.EXTERNAL_ID].orEmpty()
                        val providerInfo = AuthProvider(
                            provider = service.provider,
                            externalId = externalId,
                            metadata = authData
                        )
                        providersToSave.add(providerInfo)
                    }
                } catch (_: Exception) {
                }
            }
        }
        
        if (providersToSave.isNotEmpty()) {
            try {
                authProviderRepository.saveAll(userId, providersToSave)
            } catch (_: Exception) {
            }
        }
    }
}
