package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import com.diegoferreiracaetano.dlearn.domain.user.MovieProvider
import com.diegoferreiracaetano.dlearn.domain.user.Tmdb

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val i18nProvider: I18nProvider
) {

    suspend fun login(email: String, password: String, language: String) : AuthResponse {
        val user = userRepository.findByEmail(email)
        
        if (user != null && user.password == password) {
            // No futuro, se houver um vínculo persistente, buscaríamos o provider aqui.
            // Por enquanto, o provider virá de um fluxo de auth do TMDB separado.
            return AuthResponse(
                user = user,
                provider = null,
                accessToken = tokenService.generateAccessToken(user, null),
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
        val claims = tokenService.verifyToken(token) ?: throw AppException(
            AppError(
                code = AppErrorCode.INVALID_TOKEN,
                message = i18nProvider.getString(AppStringType.ERROR_INVALID_SESSION, language)
            )
        )
        val userId = claims["userId"] ?: throw AppException(AppError(code = AppErrorCode.INVALID_TOKEN, message = ""))
        
        val user = userRepository.findById(userId) ?: throw AppException(
            AppError(
                code = AppErrorCode.USER_NOT_FOUND,
                message = i18nProvider.getString(AppStringType.ERROR_USER_NOT_FOUND, language)
            )
        )

        val provider = if (claims["tmdbSessionId"] != null) {
            MovieProvider(Tmdb(claims["tmdbSessionId"], claims["tmdbAccountId"]))
        } else null

        return AuthResponse(
            user = user,
            provider = provider,
            accessToken = tokenService.generateAccessToken(user, provider),
            refreshToken = tokenService.generateRefreshToken(user),
            challengeRequired = false
        )
    }
}
