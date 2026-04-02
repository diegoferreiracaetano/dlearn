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
}
