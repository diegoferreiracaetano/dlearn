package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val i18nProvider: I18nProvider
) {

    suspend fun login(email: String, password: String, language: String) : AuthResponse {
        val user = userRepository.findByEmail(email)
        
        if (user != null && user.password == password) {
            return AuthResponse(
                user = user,
                accessToken = tokenService.generateAccessToken(user),
                refreshToken = tokenService.generateRefreshToken(user),
                challengeRequired = false
            )
        } else {
            throw AppException(
                AppError(
                    code = AppErrorCode.INVALID_CREDENTIALS,
                    message = i18nProvider.getString(AppStringType.ERROR_INVALID_CREDENTIALS, language)
                )
            )
        }
    }

    suspend fun refreshToken(token: String, language: String): AuthResponse {
        val userId = tokenService.verifyToken(token) ?: throw AppException(
            AppError(
                code = AppErrorCode.INVALID_TOKEN,
                message = i18nProvider.getString(AppStringType.ERROR_INVALID_SESSION, language)
            )
        )
        val user = userRepository.findById(userId) ?: throw AppException(
            AppError(
                code = AppErrorCode.USER_NOT_FOUND,
                message = i18nProvider.getString(AppStringType.ERROR_USER_NOT_FOUND, language)
            )
        )

        return AuthResponse(
            user = user,
            accessToken = tokenService.generateAccessToken(user),
            refreshToken = tokenService.generateRefreshToken(user),
            challengeRequired = false
        )
    }
}
