package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.plugins.*
import org.slf4j.LoggerFactory

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val i18n: I18nProvider
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun login(
        email: String,
        password: String,
        language: String
    ): AuthResponse {
        logger.info("Login attempt for email: $email")
        
        val user = userRepository.authenticate(email, password) ?: throw BadRequestException(
            i18n.getRawString("error_invalid_credentials", language) ?: "Invalid credentials"
        )

        val accessToken = tokenService.generateAccessToken(user)
        val refreshToken = tokenService.generateRefreshToken(user)

        return AuthResponse(
            user = user,
            accessToken = accessToken,
            refreshToken = refreshToken,
            challengeRequired = false
        )
    }

    suspend fun refreshToken(refreshToken: String, language: String): AuthResponse {
        val claims = tokenService.verifyToken(refreshToken) ?: throw BadRequestException("Invalid refresh token")
        val userId = claims["user_id"] ?: throw BadRequestException("Invalid token payload")
        val user = userRepository.findById(userId) ?: throw BadRequestException("User not found")

        val newAccessToken = tokenService.generateAccessToken(user)
        val newRefreshToken = tokenService.generateRefreshToken(user)

        return AuthResponse(
            user = user,
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            challengeRequired = false
        )
    }
}
