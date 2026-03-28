package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import java.util.UUID

class CreateUserOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val i18nProvider: I18nProvider
) {
    suspend fun createUser(name: String, email: String, password: String, language: String): AuthResponse {
        val normalizedEmail = email.trim().lowercase()
        val existingUser = userRepository.findByEmail(normalizedEmail)
        
        if (existingUser != null) {
            throw AppException(
                AppError(
                    code = AppErrorCode.EMAIL_ALREADY_IN_USE,
                    message = i18nProvider.getString(AppStringType.ERROR_EMAIL_ALREADY_REGISTERED, language)
                )
            )
        }

        val newUser = User(
            id = UUID.randomUUID().toString(),
            name = name.trim(),
            email = normalizedEmail,
            password = password
        )

        val savedUser = userRepository.save(newUser)

        return AuthResponse(
            user = savedUser,
            provider = null,
            accessToken = tokenService.generateAccessToken(savedUser, null),
            refreshToken = tokenService.generateRefreshToken(savedUser),
            challengeRequired = false
        )
    }
}
