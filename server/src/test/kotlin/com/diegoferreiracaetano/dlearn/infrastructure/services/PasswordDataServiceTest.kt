package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class PasswordDataServiceTest {

    private val challengeDataService = mockk<ChallengeDataService>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val service = PasswordDataService(challengeDataService, userRepository)

    @Test
    fun `given a valid userId and request when changePassword is called should update the user password`() = runTest {
        val request = ChangePasswordRequest("new")
        val user = User("1", "D", "d@t.com")
        coEvery { userRepository.findById("1") } returns user

        val result = service.changePassword(request, "1", null)

        assertEquals("Password changed successfully", result.message)
    }

    @Test
    fun `given an anonymous user and no token when changePassword is called should throw CHALLENGE_REQUIRED exception`() = runTest {
        val request = ChangePasswordRequest("new")

        val exception = assertFailsWith<AppException> {
            service.changePassword(request, "anonymous", null)
        }
        assertEquals(AppErrorCode.CHALLENGE_REQUIRED, exception.error.code)
    }

    @Test
    fun `given a valid challenge token when changePassword is called should update user associated with the token`() = runTest {
        val request = ChangePasswordRequest("new")
        val user = User("2", "T", "t@t.com")
        every { challengeDataService.isTokenValidated("token") } returns true
        every { challengeDataService.getUserIdByToken("token") } returns "2"
        coEvery { userRepository.findById("2") } returns user

        val result = service.changePassword(request, "anonymous", "token")

        assertEquals("Password changed successfully", result.message)
    }

    @Test
    fun `given an invalid challenge token when changePassword is called should throw INVALID_TOKEN exception`() = runTest {
        val request = ChangePasswordRequest("new")
        every { challengeDataService.isTokenValidated("bad-token") } returns false

        val exception = assertFailsWith<AppException> {
            service.changePassword(request, "anonymous", "bad-token")
        }
        assertEquals(AppErrorCode.INVALID_TOKEN, exception.error.code)
    }

    @Test
    fun `given a validated token but getUserIdByToken returns null when changePassword should throw USER_NOT_FOUND`() = runTest {
        val request = ChangePasswordRequest("new")
        every { challengeDataService.isTokenValidated("token") } returns true
        every { challengeDataService.getUserIdByToken("token") } returns null

        val exception = assertFailsWith<AppException> {
            service.changePassword(request, "anonymous", "token")
        }
        assertEquals(AppErrorCode.USER_NOT_FOUND, exception.error.code)
    }

    @Test
    fun `given a userId where findById returns null but findByEmail returns user when changePassword should succeed`() = runTest {
        val request = ChangePasswordRequest("new")
        val user = User("3", "U", "u@t.com")
        coEvery { userRepository.findById("user@example.com") } returns null
        coEvery { userRepository.findByEmail("user@example.com") } returns user

        val result = service.changePassword(request, "user@example.com", null)

        assertEquals("Password changed successfully", result.message)
    }
}
