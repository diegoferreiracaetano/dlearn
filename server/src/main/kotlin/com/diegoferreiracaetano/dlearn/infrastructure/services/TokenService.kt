package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.diegoferreiracaetano.dlearn.TokenConstants.AUDIENCE
import com.diegoferreiracaetano.dlearn.TokenConstants.CLAIM_EMAIL
import com.diegoferreiracaetano.dlearn.TokenConstants.CLAIM_USER_ID
import com.diegoferreiracaetano.dlearn.TokenConstants.ISSUER
import com.diegoferreiracaetano.dlearn.TokenConstants.SECRET
import com.diegoferreiracaetano.dlearn.domain.user.User
import java.util.Date

class TokenService(
    private val secret: String = SECRET,
    private val issuer: String = ISSUER,
    private val audience: String = AUDIENCE
) {
    private val algorithm = Algorithm.HMAC256(secret)

    fun generateAccessToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(CLAIM_USER_ID, user.id)
            .withClaim(CLAIM_EMAIL, user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRATION))
            .sign(algorithm)
    }

    fun generateRefreshToken(user: User): String {
        return JWT.create()
            .withAudience(audience)
            .withIssuer(issuer)
            .withClaim(CLAIM_USER_ID, user.id)
            .withClaim(CLAIM_EMAIL, user.email)
            .withExpiresAt(Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION))
            .sign(algorithm)
    }

    fun verifyToken(token: String): Map<String, String?>? {
        return try {
            val verifier = JWT.require(algorithm)
                .withAudience(audience)
                .withIssuer(issuer)
                .build()
            val decoded = verifier.verify(token)
            mapOf(
                CLAIM_USER_ID to decoded.getClaim(CLAIM_USER_ID).asString(),
                CLAIM_EMAIL to decoded.getClaim(CLAIM_EMAIL).asString()
            )
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private const val ACCESS_TOKEN_EXPIRATION = 15 * 60 * 1000 // 15 minutes
        private const val REFRESH_TOKEN_EXPIRATION = 30L * 24 * 60 * 60 * 1000 // 30 days
    }
}
