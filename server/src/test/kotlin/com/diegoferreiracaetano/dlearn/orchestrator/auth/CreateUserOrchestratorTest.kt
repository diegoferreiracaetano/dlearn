package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.plugins.BadRequestException
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class CreateUserOrchestratorTest {

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val tokenService = mockk<TokenService>(relaxed = true)
    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val orchestrator = CreateUserOrchestrator(userRepository, tokenService, i18n)

    @Test
    fun `given a new user when create is called should return a valid authentication response`() = runTest {
        coEvery { userRepository.findByEmail("new@test.com") } returns null
        coEvery { tokenService.generateAccessToken(any()) } returns "access"
        coEvery { tokenService.generateRefreshToken(any()) } returns "refresh"

        val result = orchestrator.create("New", "new@test.com", "123", "en")

        assertEquals("new@test.com", result.user?.email)
        assertEquals("access", result.accessToken)
        assertEquals("refresh", result.refreshToken)
    }

    @Test
    fun `given an existing email when create is called should throw BadRequestException`() = runTest {
        coEvery { userRepository.findByEmail("existing@test.com") } returns mockk()

        assertFailsWith<BadRequestException> {
            orchestrator.create("Existing", "existing@test.com", "123", "en")
        }
    }
}
