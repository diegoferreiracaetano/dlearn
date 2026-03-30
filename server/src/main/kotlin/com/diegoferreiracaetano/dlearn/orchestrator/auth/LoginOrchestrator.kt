package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.plugins.*

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val i18n: I18nProvider
) {
    suspend fun login(
        email: String,
        password: String,
        language: String,
        tmdbUsername: String? = null,
        tmdbPassword: String? = null
    ): AuthResponse {
        val user = userRepository.authenticate(email, password) ?: throw BadRequestException(
            i18n.getRawString("error_invalid_credentials", language).orEmpty()
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
        val claims = tokenService.verifyToken(refreshToken) ?: throw BadRequestException(
            i18n.getRawString("error_invalid_refresh_token", language).orEmpty()
        )
        val userId = claims["user_id"] ?: throw BadRequestException(
            i18n.getRawString("error_invalid_token_payload", language).orEmpty()
        )
        val user = userRepository.findById(userId) ?: throw BadRequestException(
            i18n.getRawString("error_user_not_found", language).orEmpty()
        )

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
