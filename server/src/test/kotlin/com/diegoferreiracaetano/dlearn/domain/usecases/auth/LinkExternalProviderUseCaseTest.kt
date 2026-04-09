package com.diegoferreiracaetano.dlearn.domain.usecases.auth

import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider
import com.diegoferreiracaetano.dlearn.infrastructure.auth.AuthProviderSyncService
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class LinkExternalProviderUseCaseTest {

    private val authProviderSyncService = mockk<AuthProviderSyncService>(relaxed = true)
    private val authProviderRepository = mockk<AuthProviderRepository>(relaxed = true)
    private val testDispatcher = UnconfinedTestDispatcher()

    private val useCase = LinkExternalProviderUseCase(
        authProviderSyncService = authProviderSyncService,
        authProviderRepository = authProviderRepository,
        ioDispatcher = testDispatcher,
    )

    @Test
    fun `given a userId when execute is called should fetch existing providers from repository`() = runTest(
        testDispatcher
    ) {
        coEvery { authProviderRepository.findByUserId("user1") } returns emptyList()

        useCase.execute("user1")

        coVerify { authProviderRepository.findByUserId("user1") }
    }

    @Test
    fun `given a userId and metadata when execute is called should merge and call sync service`() = runTest(
        testDispatcher
    ) {
        val existingProvider = AuthProvider(
            provider = AccountProvider.GOOGLE,
            externalId = "ext-123",
            metadata = mapOf("existing_key" to "existing_value"),
        )
        coEvery { authProviderRepository.findByUserId("user1") } returns listOf(existingProvider)

        useCase.execute("user1", metadata = mapOf("new_key" to "new_value"))

        coVerify {
            authProviderSyncService.discoverAndSaveProviders(
                userId = "user1",
                metadata = match { it.containsKey("existing_key") && it.containsKey("new_key") },
            )
        }
    }

    @Test
    fun `given a repository that throws when execute is called should not propagate the exception`() = runTest(
        testDispatcher
    ) {
        coEvery { authProviderRepository.findByUserId(any()) } throws RuntimeException("DB error")

        useCase.execute("user1")

        coVerify(exactly = 0) { authProviderSyncService.discoverAndSaveProviders(any(), any()) }
    }

    @Test
    fun `given no existing providers when execute is called should call sync service with only passed metadata`() = runTest(
        testDispatcher
    ) {
        coEvery { authProviderRepository.findByUserId("user1") } returns emptyList()

        useCase.execute("user1", metadata = mapOf("token" to "abc123"))

        coVerify {
            authProviderSyncService.discoverAndSaveProviders(
                userId = "user1",
                metadata = mapOf("token" to "abc123"),
            )
        }
    }
}
