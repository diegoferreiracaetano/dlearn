package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.plugins.*
import org.slf4j.LoggerFactory
import java.util.*

class CreateUserOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val i18n: I18nProvider
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun create(
        name: String,
        email: String,
        password: String,
        language: String,
        tmdbUsername: String? = null,
        tmdbPassword: String? = null
    ): AuthResponse {
        logger.info("Starting user creation for email: $email")
        
        val existingUser = userRepository.findByEmail(email)
        if (existingUser != null) {
            throw BadRequestException(i18n.getRawString("error_user_already_exists", language) ?: "User already exists")
        }

        val newUser = User(
            id = UUID.randomUUID().toString(),
            name = name,
            email = email
        )
        
        userRepository.save(newUser, password)
        logger.info("User ${newUser.id} saved to repository")

        val accessToken = tokenService.generateAccessToken(newUser)
        val refreshToken = tokenService.generateRefreshToken(newUser)
        
        return AuthResponse(
            user = newUser,
            accessToken = accessToken,
            refreshToken = refreshToken,
            challengeRequired = false
        )
    }
}
