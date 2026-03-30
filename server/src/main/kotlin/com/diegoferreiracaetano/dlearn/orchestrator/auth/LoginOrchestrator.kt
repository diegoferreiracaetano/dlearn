package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.SocialAuthConstants
import com.diegoferreiracaetano.dlearn.TokenConstants
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.auth.LinkExternalProviderUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import java.util.*
import kotlinx.serialization.json.*

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val linkExternalProviderUseCase: LinkExternalProviderUseCase,
    private val i18n: I18nProvider
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun login(
        email: String,
        password: String,
        language: String
    ): AuthResponse {
        val user = userRepository.authenticate(email, password) ?: throw AppException(
            AppError(
                code = AppErrorCode.INVALID_CREDENTIALS,
                message = i18n.getString(AppStringType.ERROR_INVALID_CREDENTIALS, language)
            )
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
        val (email, name, picture) = extractSocialUserInfo(idToken, provider, language)

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

        val metadata = mutableMapOf<String, String>()
        metadata[MetadataKeys.AUTH_TYPE] = provider
        metadata[MetadataKeys.EXTERNAL_ID] = email
        accessToken?.let { metadata[MetadataKeys.ACCESS_TOKEN] = it }
        metadata[MetadataKeys.ID_TOKEN] = idToken

        linkExternalProviderUseCase.execute(userId = user!!.id, metadata = metadata)

        val newAccessToken = tokenService.generateAccessToken(user)
        val newRefreshToken = tokenService.generateRefreshToken(user)

        return AuthResponse(
            user = user,
            accessToken = newAccessToken,
            refreshToken = newRefreshToken,
            challengeRequired = false
        )
    }

    private fun extractSocialUserInfo(idToken: String, provider: String, language: String): Triple<String, String, String?> {
        try {
            val parts = idToken.split(SocialAuthConstants.JWT_SEPARATOR)
            if (parts.size < 2) throw Exception("Invalid JWT format")
            
            val payload = String(Base64.getDecoder().decode(parts[1]))
            val jsonObject = json.parseToJsonElement(payload).jsonObject
            
            val email = jsonObject[SocialAuthConstants.CLAIM_EMAIL]?.jsonPrimitive?.content
                ?: throw Exception("Email not found in social token")
                
            val firstName = jsonObject[SocialAuthConstants.CLAIM_GIVEN_NAME]?.jsonPrimitive?.content ?: ""
            val lastName = jsonObject[SocialAuthConstants.CLAIM_FAMILY_NAME]?.jsonPrimitive?.content ?: ""
            
            val name = jsonObject[SocialAuthConstants.CLAIM_NAME]?.jsonPrimitive?.content ?: 
                       "$firstName $lastName".trim().ifEmpty { 
                           provider.replaceFirstChar { it.uppercase() } + SocialAuthConstants.DEFAULT_NAME_SUFFIX
                       }
            val picture = jsonObject[SocialAuthConstants.CLAIM_PICTURE]?.jsonPrimitive?.content
            
            return Triple(email, name, picture)
        } catch (e: Exception) {
            throw AppException(
                AppError(
                    code = AppErrorCode.SOCIAL_AUTH_FAILED,
                    message = i18n.getString(AppStringType.ERROR_SOCIAL_AUTH_FAILED, language)
                ),
                cause = e
            )
        }
    }

    suspend fun refreshToken(refreshToken: String, language: String): AuthResponse {
        val claims = tokenService.verifyToken(refreshToken) ?: throw AppException(
            AppError(
                code = AppErrorCode.EXPIRED_TOKEN,
                message = i18n.getString(AppStringType.ERROR_INVALID_REFRESH_TOKEN, language)
            )
        )
        
        val userId = claims[TokenConstants.CLAIM_USER_ID] ?: throw AppException(
            AppError(
                code = AppErrorCode.INVALID_TOKEN,
                message = i18n.getString(AppStringType.ERROR_INVALID_TOKEN_PAYLOAD, language)
            )
        )
        
        val user = userRepository.findById(userId) ?: throw AppException(
            AppError(
                code = AppErrorCode.USER_NOT_FOUND,
                message = i18n.getString(AppStringType.ERROR_USER_NOT_FOUND, language)
            )
        )

        linkExternalProviderUseCase.execute(userId = user.id)

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
