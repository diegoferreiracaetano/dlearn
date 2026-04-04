package com.diegoferreiracaetano.dlearn.data.session

import com.russhwolf.settings.Settings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class SettingsSessionStorageTest {

    private val settings = mockk<Settings>(relaxed = true)
    private val sessionStorage = SettingsSessionStorage(settings)

    @Test
    fun `when saveToken is called should put string in settings`() = runTest {
        val token = "test_token"
        sessionStorage.saveToken(token)
        verify { settings.putString("auth_token", token) }
    }

    @Test
    fun `when getToken is called should return from settings`() {
        val token = "test_token"
        every { settings.getStringOrNull("auth_token") } returns token
        assertEquals(token, sessionStorage.getToken())
    }

    @Test
    fun `when hasSession is called should check key in settings`() {
        every { settings.hasKey("auth_token") } returns true
        assertTrue(sessionStorage.hasSession())
        
        every { settings.hasKey("auth_token") } returns false
        assertFalse(sessionStorage.hasSession())
    }

    @Test
    fun `when saveUser is called should put user json in settings`() = runTest {
        val userJson = "{\"id\":\"1\"}"
        sessionStorage.saveUser(userJson)
        verify { settings.putString("user", userJson) }
    }

    @Test
    fun `when getUser is called should return from settings`() {
        val userJson = "{\"id\":\"1\"}"
        every { settings.getStringOrNull("user") } returns userJson
        assertEquals(userJson, sessionStorage.getUser())
    }

    @Test
    fun `when clear is called should remove auth_token from settings`() = runTest {
        sessionStorage.clear()
        verify { settings.remove("auth_token") }
    }
}
