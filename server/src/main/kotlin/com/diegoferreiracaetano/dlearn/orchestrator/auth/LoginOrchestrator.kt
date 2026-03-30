package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.plugins.*
import java.util.*
import kotlinx.serialization.json.*

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val i18n: I18nProvider
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun login(
        email: String,
        password: String,
        language: String
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

    suspend fun socialLogin(
        provider: String,
        idToken: String,
        accessToken: String?,
        language: String
    ): AuthResponse {
        
        // Extrai informações do idToken (JWT) se for Google ou Apple
        var email = "social_$provider@example.com"
        var name = provider.replaceFirstChar { it.uppercase() } + " User"
        var picture: String? = null

        try {
            if (provider == "google" || provider == "apple") {
                val parts = idToken.split(".")
                if (parts.size >= 2) {
                    val payload = String(Base64.getDecoder().decode(parts[1]))
                    val jsonObject = json.parseToJsonElement(payload).jsonObject
                    
                    email = jsonObject["email"]?.jsonPrimitive?.content ?: email
                    name = jsonObject["name"]?.jsonPrimitive?.content ?: 
                           "${jsonObject["given_name"]?.jsonPrimitive?.content ?: ""} ${jsonObject["family_name"]?.jsonPrimitive?.content ?: ""}".trim().ifEmpty { name }
                    picture = jsonObject["picture"]?.jsonPrimitive?.content
                }
            }
        } catch (e: Exception) {
            // Se falhar o parse, mantém os dados default do provider
        }

        var user = userRepository.findByEmail(email)
        if (user == null) {
            user = userRepository.save(
                User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    imageUrl = picture
                ),
                password = UUID.randomUUID().toString()
            )
        }

        val newAccessToken = tokenService.generateAccessToken(user)
        val newRefreshToken = tokenService.generateRefreshToken(user)

        return AuthResponse(
            user = user,
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
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
