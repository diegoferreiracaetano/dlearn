package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.diegoferreiracaetano.dlearn.domain.user.User
import java.util.*

class TokenService(
    private val secret: String = "dlearn-secret-key-change-it-in-prod",
    private val issuer: String = "com.diegoferreiracaetano.dlearn",
    private val audience: String = "dlearn-audience"
) {
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", user.id)
            .withClaim("email", user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .sign(algorithm)
    }

    fun generateRefreshToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim("userId", user.id)
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .sign(algorithm)
    }

    fun verifyToken(token: String): String? {
        return try {
            val verifier = JWT.require(algorithm)
                .withAudience(audience)
                .withIssuer(issuer)
                .build()
            val decoded = verifier.verify(token)
            decoded.getClaim("userId").asString()
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000 // 15 minutes
        private const val REFRESH_TOKEN_EXPIRATION = 30L * 24 * 60 * 60 * 1000 // 30 days
    }
}
