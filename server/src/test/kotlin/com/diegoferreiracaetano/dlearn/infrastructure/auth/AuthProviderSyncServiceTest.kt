package com.diegoferreiracaetano.dlearn.infrastructure.auth

import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.Feature
import com.diegoferreiracaetano.dlearn.infrastructure.services.FeatureToggleService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class AuthProviderSyncServiceTest {

    private val featureToggleService = mockk<FeatureToggleService>()
    private val authProviderRepository = mockk<AuthProviderRepository>(relaxed = true)
    private val externalAuthService = mockk<ExternalAuthService>(relaxed = true)

    private val service = AuthProviderSyncService(
        authServices = listOf(externalAuthService),
        featureToggleService = featureToggleService,
        authProviderRepository = authProviderRepository,
    )

    @Test
    fun `given feature disabled when discoverAndSaveProviders is called should not call any auth service`() = runTest {
        every { featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC) } returns false

        service.discoverAndSaveProviders("user1", emptyMap())

        coVerify(exactly = 0) { externalAuthService.authenticate(any()) }
        coVerify(exactly = 0) { authProviderRepository.saveAll(any(), any()) }
    }

    @Test
    fun `given feature enabled and service cannot handle when discoverAndSaveProviders is called should not save`() = runTest {
        every { featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC) } returns true
        every { externalAuthService.canHandle(any()) } returns false

        service.discoverAndSaveProviders("user1", mapOf("key" to "value"))

        coVerify(exactly = 0) { authProviderRepository.saveAll(any(), any()) }
    }

    @Test
    fun `given feature enabled and service can handle when discoverAndSaveProviders is called should save providers`() = runTest {
        every { featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC) } returns true
        every { externalAuthService.canHandle(any()) } returns true
        coEvery { externalAuthService.authenticate(any()) } returns mapOf(
            "external_id" to "ext-123",
            "token" to "abc",
        )

        service.discoverAndSaveProviders("user1", mapOf("key" to "value"))

        coVerify(exactly = 1) { authProviderRepository.saveAll("user1", any()) }
    }

    @Test
    fun `given feature enabled and service throws when discoverAndSaveProviders is called should not propagate exception`() = runTest {
        every { featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC) } returns true
        every { externalAuthService.canHandle(any()) } returns true
        coEvery { externalAuthService.authenticate(any()) } throws RuntimeException("Auth failed")

        service.discoverAndSaveProviders("user1", emptyMap())

        coVerify(exactly = 0) { authProviderRepository.saveAll(any(), any()) }
    }

    @Test
    fun `given feature enabled and service returns empty data when discoverAndSaveProviders is called should not save`() = runTest {
        every { featureToggleService.isEnabled(Feature.EXTERNAL_AUTH_SYNC) } returns true
        every { externalAuthService.canHandle(any()) } returns true
        coEvery { externalAuthService.authenticate(any()) } returns emptyMap()

        service.discoverAndSaveProviders("user1", emptyMap())

        coVerify(exactly = 0) { authProviderRepository.saveAll(any(), any()) }
    }
}
