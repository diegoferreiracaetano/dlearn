package com.diegoferreiracaetano.dlearn.domain.session

import com.diegoferreiracaetano.dlearn.domain.auth.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.user.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionManagerTest {

    private val accountProvider = mockk<AccountProvider>(relaxed = true)
    private val sessionManager = SessionManager(accountProvider)

    @Test
    fun `when initialize is called should update isLoggedIn based on provider`() = runTest {
        coEvery { accountProvider.hasAccount() } returns true
        sessionManager.initialize()
        assertTrue(sessionManager.isLoggedIn.value)

        coEvery { accountProvider.hasAccount() } returns false
        sessionManager.initialize()
        assertFalse(sessionManager.isLoggedIn.value)
    }

    @Test
    fun `when login is called should save account and set isLoggedIn to true`() = runTest {
        val user = User(id = "1", email = "test@test.com", name = "Test")
        sessionManager.login(user, "access", "refresh")

        coVerify(exactly = 1) { accountProvider.saveAccount(user, "access", "refresh") }
        assertTrue(sessionManager.isLoggedIn.value)
    }

    @Test
    fun `when logout is called should clear account and set isLoggedIn to false`() = runTest {
        sessionManager.logout()

        coVerify(exactly = 1) { accountProvider.clearAccount() }
        assertFalse(sessionManager.isLoggedIn.value)
    }

    @Test
    fun `when user is called should return user from provider`() = runTest {
        val user = User(id = "1", email = "test@test.com", name = "Test")
        coEvery { accountProvider.getUser() } returns user

        val result = sessionManager.user()
        assertEquals(user, result)
    }

    @Test
    fun `when user is called and no user exists should throw AppException`() = runTest {
        coEvery { accountProvider.getUser() } returns null

        val exception = assertFailsWith<AppException> {
            sessionManager.user()
        }
        assertEquals(AppErrorCode.USER_NOT_FOUND, exception.error.code)
    }

    @Test
    fun `when token is called should return token from provider`() = runTest {
        val token = "token"
        coEvery { accountProvider.getAccessToken() } returns token

        val result = sessionManager.token()
        assertEquals(token, result)
    }

    @Test
    fun `when refreshToken is called should return refresh token from provider`() = runTest {
        val refreshToken = "refresh"
        coEvery { accountProvider.getRefreshToken() } returns refreshToken

        val result = sessionManager.refreshToken()
        assertEquals(refreshToken, result)
    }
}
