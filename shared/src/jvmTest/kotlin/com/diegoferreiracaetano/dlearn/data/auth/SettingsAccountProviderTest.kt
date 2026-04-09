package com.diegoferreiracaetano.dlearn.data.auth

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.util.toJson
import com.russhwolf.settings.Settings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SettingsAccountProviderTest {

    private val settings = mockk<Settings>(relaxed = true)
    private lateinit var provider: SettingsAccountProvider

    @Before
    fun setup() {
        provider = SettingsAccountProvider(settings)
    }

    @Test
    fun `when saveAccount is called should store all values in settings`() = runTest {
        val user = User(id = "1", email = "test@test.com", name = "Test")
        val accessToken = "access"
        val refreshToken = "refresh"

        provider.saveAccount(user, accessToken, refreshToken)

        verify { settings.putString("access_token", accessToken) }
        verify { settings.putString("refresh_token", refreshToken) }
        verify { settings.putString("user_data", user.toJson()) }
    }

    @Test
    fun `when getAccessToken is called should return value from settings`() = runTest {
        every { settings.getStringOrNull("access_token") } returns "token123"
        assertEquals("token123", provider.getAccessToken())
    }

    @Test
    fun `when getRefreshToken is called should return value from settings`() = runTest {
        every { settings.getStringOrNull("refresh_token") } returns "refresh123"
        assertEquals("refresh123", provider.getRefreshToken())
    }

    @Test
    fun `when getUser is called should decode and return user`() = runTest {
        val user = User(id = "1", email = "test@test.com", name = "Test")
        every { settings.getStringOrNull("user_data") } returns user.toJson()

        val result = provider.getUser()
        assertEquals(user, result)
    }

    @Test
    fun `when getUser is called and settings is empty should return null`() = runTest {
        every { settings.getStringOrNull("user_data") } returns null

        val result = provider.getUser()
        assertNull(result)
    }

    @Test
    fun `when clearAccount is called should remove all keys`() = runTest {
        provider.clearAccount()

        verify { settings.remove("access_token") }
        verify { settings.remove("refresh_token") }
        verify { settings.remove("user_data") }
    }

    @Test
    fun `when hasAccount is called should return true if access_token exists`() = runTest {
        every { settings.hasKey("access_token") } returns false
        assertFalse(provider.hasAccount())

        every { settings.hasKey("access_token") } returns true
        assertTrue(provider.hasAccount())
    }
}
