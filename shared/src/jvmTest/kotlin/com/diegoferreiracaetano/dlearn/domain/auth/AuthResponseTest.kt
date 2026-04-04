package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.user.User
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class AuthResponseTest {

    @Test
    fun `AuthResponse holds user and tokens`() {
        val user = User("1", "Test", "test@example.com")
        val response = AuthResponse(user, "access", "refresh", false)
        assertEquals(user, response.user)
        assertEquals("access", response.accessToken)
        assertEquals("refresh", response.refreshToken)
        assertFalse(response.challengeRequired)
    }

    @Test
    fun `AuthResponse defaults are null and false`() {
        val response = AuthResponse()
        assertNull(response.user)
        assertNull(response.accessToken)
        assertNull(response.refreshToken)
        assertFalse(response.challengeRequired)
    }
}
