package com.diegoferreiracaetano.dlearn.data.session

import com.russhwolf.settings.Settings
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SettingsSessionStorageTest {

    private val settings = mockk<Settings>(relaxed = true)
    private val subject = SettingsSessionStorage(settings)

    @Test
    fun `given a token when saveToken is called should store it in settings`() = runTest {
        subject.saveToken("token123")
        verify { settings.putString("auth_token", "token123") }
    }

    @Test
    fun `given a token exists when getToken is called should return it`() {
        every { settings.getStringOrNull("auth_token") } returns "token123"
        assertEquals("token123", subject.getToken())
    }

    @Test
    fun `given a token exists when hasSession is called should return true`() {
        every { settings.hasKey("auth_token") } returns true
        assertTrue(subject.hasSession())
    }

    @Test
    fun `given a user json when saveUser is called should store it`() = runTest {
        subject.saveUser("{}")
        verify { settings.putString("user", "{}") }
    }

    @Test
    fun `given a user exists when getUser is called should return it`() {
        every { settings.getStringOrNull("user") } returns "{}"
        assertEquals("{}", subject.getUser())
    }

    @Test
    fun `when clear is called should remove auth token`() = runTest {
        subject.clear()
        verify { settings.remove("auth_token") }
    }
}
