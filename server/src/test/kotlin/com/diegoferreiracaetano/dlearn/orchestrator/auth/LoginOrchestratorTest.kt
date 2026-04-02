package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.auth.LinkExternalProviderUseCase
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginOrchestratorTest {

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val tokenService = mockk<TokenService>(relaxed = true)
    private val linkExternalProviderUseCase = mockk<LinkExternalProviderUseCase>(relaxed = true)
    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val orchestrator = LoginOrchestrator(userRepository, tokenService, linkExternalProviderUseCase, i18n)

    @Test
    fun `given valid credentials when login should return auth response`() = runTest {
        val user = User("1", "Diego", "diego@test.com")
        coEvery { userRepository.authenticate("diego@test.com", "123") } returns user
        every { tokenService.generateAccessToken(user) } returns "access"
        every { tokenService.generateRefreshToken(user) } returns "refresh"

        val result = orchestrator.login("diego@test.com", "123", "en")

        assertEquals(user, result.user)
        assertEquals("access", result.accessToken)
        assertEquals("refresh", result.refreshToken)
    }

    @Test
    fun `given invalid credentials when login should throw exception`() = runTest {
        coEvery { userRepository.authenticate(any(), any()) } returns null

        assertFailsWith<AppException> {
            orchestrator.login("wrong@test.com", "wrong", "en")
        }
    }
}
