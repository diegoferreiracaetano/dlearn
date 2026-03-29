package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.auth.AuthProviderSyncService
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.server.plugins.*
import org.slf4j.LoggerFactory

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val authProviderSyncService: AuthProviderSyncService,
    private val i18n: I18nProvider
) {
    private val logger = LoggerFactory.getLogger(javaClass)

    suspend fun login(
        email: String,
        password: String,
        metadata: Map<String, String>,
        language: String
    ): AuthResponse {
        logger.info("Login attempt for email: $email")
        
        val user = userRepository.authenticate(email, password) ?: throw BadRequestException(
            i18n.getRawString("error_invalid_credentials", language) ?: "Invalid credentials"
        )

        // Sincronização de Provedores Pós-Login (Resiliência de Backend)
        // Se o front não enviou metadata, injetamos as credenciais de teste para garantir o vínculo
        val finalMetadata = if (metadata.isEmpty()) {
            logger.info("Metadata empty during login for ${user.id}, injecting default TMDB credentials")
            mapOf(
                MetadataKeys.EXTERNAL_USERNAME to "diegoferreiracaetano",
                MetadataKeys.EXTERNAL_PASSWORD to "D@f78326244"
            )
        } else {
            metadata
        }
        
        // A sincronização ocorre APÓS o login ser validado com sucesso
        authProviderSyncService.discoverAndSaveProviders(user.id, finalMetadata)

        val accessToken = tokenService.generateAccessToken(user)
        val refreshToken = tokenService.generateRefreshToken(user)

        return AuthResponse(
            user = user,
            accessToken = accessToken,
            refreshToken = refreshToken,
            challengeRequired = false
        )
    }
}
