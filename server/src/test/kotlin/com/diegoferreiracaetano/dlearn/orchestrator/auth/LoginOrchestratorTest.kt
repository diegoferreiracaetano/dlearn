package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.TokenConstants
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
import java.util.Base64
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class LoginOrchestratorTest {

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val tokenService = mockk<TokenService>(relaxed = true)
    private val linkExternalProviderUseCase = mockk<LinkExternalProviderUseCase>(relaxed = true)
    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val orchestrator = LoginOrchestrator(userRepository, tokenService, linkExternalProviderUseCase, i18n)

    private val user = User("1", "Diego", "diego@test.com")

    @Test
    fun `given valid credentials when login should return auth response`() = runTest {
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

    @Test
    fun `given a valid idToken when socialLogin should return auth response for existing user`() = runTest {
        val payloadJson = """{"email":"social@test.com","name":"Social User"}"""
        val encodedPayload = Base64.getEncoder().encodeToString(payloadJson.toByteArray())
        val idToken = "header.$encodedPayload.signature"

        coEvery { userRepository.findByEmail("social@test.com") } returns user
        every { tokenService.generateAccessToken(user) } returns "access"
        every { tokenService.generateRefreshToken(user) } returns "refresh"

        val result = orchestrator.socialLogin("google", idToken, null, "en")

        assertEquals(user, result.user)
        assertEquals("access", result.accessToken)
    }

    @Test
    fun `given a valid idToken with new user when socialLogin should create and return the user`() = runTest {
        val payloadJson = """{"email":"new@test.com","name":"New User","given_name":"New","family_name":"User"}"""
        val encodedPayload = Base64.getEncoder().encodeToString(payloadJson.toByteArray())
        val idToken = "header.$encodedPayload.signature"
        val newUser = User("new-id", "New User", "new@test.com")

        coEvery { userRepository.findByEmail("new@test.com") } returns null
        coEvery { userRepository.save(any(), any()) } returns newUser
        every { tokenService.generateAccessToken(newUser) } returns "access"
        every { tokenService.generateRefreshToken(newUser) } returns "refresh"

        val result = orchestrator.socialLogin("google", idToken, "access-token", "en")

        assertEquals(newUser, result.user)
    }

    @Test
    fun `given an idToken with missing email when socialLogin should throw AppException`() = runTest {
        val payloadJson = """{"name":"No Email"}"""
        val encodedPayload = Base64.getEncoder().encodeToString(payloadJson.toByteArray())
        val idToken = "header.$encodedPayload.signature"

        assertFailsWith<AppException> {
            orchestrator.socialLogin("google", idToken, null, "en")
        }
    }

    @Test
    fun `given an invalid idToken format when socialLogin should throw AppException`() = runTest {
        assertFailsWith<AppException> {
            orchestrator.socialLogin("google", "invalid-token", null, "en")
        }
    }

    @Test
    fun `given a valid refresh token when refreshToken should return new auth response`() = runTest {
        coEvery { tokenService.verifyToken("valid-refresh") } returns mapOf(TokenConstants.CLAIM_USER_ID to "1")
        coEvery { userRepository.findById("1") } returns user
        every { tokenService.generateAccessToken(user) } returns "new-access"
        every { tokenService.generateRefreshToken(user) } returns "new-refresh"

        val result = orchestrator.refreshToken("valid-refresh", "en")

        assertEquals(user, result.user)
        assertEquals("new-access", result.accessToken)
        assertEquals("new-refresh", result.refreshToken)
    }

    @Test
    fun `given an expired refresh token when refreshToken should throw AppException`() = runTest {
        coEvery { tokenService.verifyToken("expired-token") } returns null

        assertFailsWith<AppException> {
            orchestrator.refreshToken("expired-token", "en")
        }
    }

    @Test
    fun `given a token without userId claim when refreshToken should throw AppException`() = runTest {
        coEvery { tokenService.verifyToken("token") } returns mapOf("other_claim" to "value")

        assertFailsWith<AppException> {
            orchestrator.refreshToken("token", "en")
        }
    }

    @Test
    fun `given a userId that does not exist when refreshToken should throw AppException`() = runTest {
        coEvery { tokenService.verifyToken("token") } returns mapOf(TokenConstants.CLAIM_USER_ID to "ghost")
        coEvery { userRepository.findById("ghost") } returns null

        assertFailsWith<AppException> {
            orchestrator.refreshToken("token", "en")
        }
    }

    @Test
    fun `given an idToken with given_name and family_name but no name when socialLogin should compose name from first and last`() = runTest {
        val payloadJson = """{"email":"composed@test.com","given_name":"John","family_name":"Doe"}"""
        val encodedPayload = Base64.getEncoder().encodeToString(payloadJson.toByteArray())
        val idToken = "header.$encodedPayload.signature"
        val newUser = User("id", "John Doe", "composed@test.com")

        coEvery { userRepository.findByEmail("composed@test.com") } returns null
        coEvery { userRepository.save(any(), any()) } returns newUser
        every { tokenService.generateAccessToken(newUser) } returns "access"
        every { tokenService.generateRefreshToken(newUser) } returns "refresh"

        val result = orchestrator.socialLogin("google", idToken, null, "en")

        assertEquals(newUser, result.user)
    }

    @Test
    fun `given an idToken with no name fields at all when socialLogin should use provider as name fallback`() = runTest {
        val payloadJson = """{"email":"nofull@test.com"}"""
        val encodedPayload = Base64.getEncoder().encodeToString(payloadJson.toByteArray())
        val idToken = "header.$encodedPayload.signature"
        val newUser = User("id", "Google User", "nofull@test.com")

        coEvery { userRepository.findByEmail("nofull@test.com") } returns null
        coEvery { userRepository.save(any(), any()) } returns newUser
        every { tokenService.generateAccessToken(newUser) } returns "access"
        every { tokenService.generateRefreshToken(newUser) } returns "refresh"

        val result = orchestrator.socialLogin("google", idToken, null, "en")

        assertEquals(newUser, result.user)
    }
}
