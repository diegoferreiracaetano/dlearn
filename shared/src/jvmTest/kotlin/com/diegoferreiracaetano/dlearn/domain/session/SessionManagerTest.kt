package com.diegoferreiracaetano.dlearn.domain.session

import com.diegoferreiracaetano.dlearn.domain.auth.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.user.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SessionManagerTest {

    private val accountProvider: AccountProvider = mockk()
    private lateinit var sessionManager: SessionManager

    private val user = User(
        id = "1",
        name = "Test User",
        email = "test@example.com"
    )

    @Before
    fun setup() {
        sessionManager = SessionManager(accountProvider)
    }

    @Test
    fun `initialize should update isLoggedIn when account exists`() = runTest {
        coEvery { accountProvider.hasAccount() } returns true

        sessionManager.initialize()

        assertTrue(sessionManager.isLoggedIn.value)
    }

    @Test
    fun `initialize should update isLoggedIn when account does not exist`() = runTest {
        coEvery { accountProvider.hasAccount() } returns false

        sessionManager.initialize()

        assertFalse(sessionManager.isLoggedIn.value)
    }

    @Test
    fun `login should save account and set isLoggedIn to true`() = runTest {
        coEvery { accountProvider.saveAccount(any(), any(), any()) } returns Unit

        sessionManager.login(user, "access", "refresh")

        assertTrue(sessionManager.isLoggedIn.value)
        coVerify { accountProvider.saveAccount(user, "access", "refresh") }
    }

    @Test
    fun `logout should clear account and set isLoggedIn to false`() = runTest {
        coEvery { accountProvider.clearAccount() } returns Unit

        sessionManager.logout()

        assertFalse(sessionManager.isLoggedIn.value)
        coVerify { accountProvider.clearAccount() }
    }

    @Test
    fun `user should return user when logged in`() = runTest {
        coEvery { accountProvider.getUser() } returns user

        val result = sessionManager.user()

        assertEquals(user, result)
    }

    @Test
    fun `user should throw exception when not logged in`() = runTest {
        coEvery { accountProvider.getUser() } returns null

        assertFailsWith<AppException> {
            sessionManager.user()
        }
    }

    @Test
    fun `token should return access token`() = runTest {
        coEvery { accountProvider.getAccessToken() } returns "access"

        val result = sessionManager.token()

        assertEquals("access", result)
    }

    @Test
    fun `refreshToken should return refresh token`() = runTest {
        coEvery { accountProvider.getRefreshToken() } returns "refresh"

        val result = sessionManager.refreshToken()

        assertEquals("refresh", result)
    }
}
