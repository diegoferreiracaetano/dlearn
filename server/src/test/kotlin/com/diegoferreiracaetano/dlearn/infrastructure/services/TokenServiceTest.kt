package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.TokenConstants
import com.diegoferreiracaetano.dlearn.domain.user.User
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TokenServiceTest {

    private val service = TokenService("secret", "issuer", "audience")
    private val user = User("1", "Diego", "diego@test.com")

    @Test
    fun `given a user when generateAccessToken is called should return a valid access token JWT`() {
        val token = service.generateAccessToken(user)
        assertNotNull(token)
        
        val claims = service.verifyToken(token)
        assertNotNull(claims)
        assertEquals("1", claims[TokenConstants.CLAIM_USER_ID])
        assertEquals("diego@test.com", claims[TokenConstants.CLAIM_EMAIL])
    }

    @Test
    fun `given a user when generateRefreshToken is called should return a valid refresh token JWT`() {
        val token = service.generateRefreshToken(user)
        assertNotNull(token)
        
        val claims = service.verifyToken(token)
        assertNotNull(claims)
        assertEquals("1", claims[TokenConstants.CLAIM_USER_ID])
    }

    @Test
    fun `given an invalid token when verifyToken is called should return null`() {
        val claims = service.verifyToken("invalid")
        assertNull(claims)
    }
}
