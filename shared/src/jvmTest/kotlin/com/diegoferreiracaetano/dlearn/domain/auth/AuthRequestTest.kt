package com.diegoferreiracaetano.dlearn.domain.auth

import kotlin.test.Test
import kotlin.test.assertEquals

class AuthRequestTest {

    @Test
    fun `AuthRequest holds email and password`() {
        val request = AuthRequest("test@example.com", "password123")
        assertEquals("test@example.com", request.email)
        assertEquals("password123", request.password)
    }

    @Test
    fun `RefreshTokenRequest holds refresh token`() {
        val request = RefreshTokenRequest("some_refresh_token")
        assertEquals("some_refresh_token", request.refreshToken)
    }
}
