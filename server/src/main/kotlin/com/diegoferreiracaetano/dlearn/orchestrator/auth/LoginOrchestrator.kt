package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.SocialAuthConstants
import com.diegoferreiracaetano.dlearn.TokenConstants
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.auth.LinkExternalProviderUseCase
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.TokenService
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.util.Base64
import java.util.UUID

class LoginOrchestrator(
    private val userRepository: UserRepository,
    private val tokenService: TokenService,
    private val linkExternalProviderUseCase: LinkExternalProviderUseCase,
    private val i18n: I18nProvider,
) {
    private val json = Json { ignoreUnknownKeys = true }

    suspend fun login(
        email: String,
        password: String,
        language: String,
    ): AuthResponse {
        val user =
            userRepository.authenticate(email, password) ?: throw AppException(
                AppError(
                    code = AppErrorCode.INVALID_CREDENTIALS,
                    message = i18n.getString(AppStringType.ERROR_INVALID_CREDENTIALS, language),
                ),
            )

        return generateAuthResponse(user)
    }

    suspend fun socialLogin(
        provider: String,
        idToken: String,
        accessToken: String?,
        language: String,
    ): AuthResponse {
        val (email, name, picture) = extractSocialUserInfo(idToken, provider, language)

        val user =
            userRepository.findByEmail(email) ?: userRepository.save(
                User(
                    id = UUID.randomUUID().toString(),
                    name = name,
                    email = email,
                    imageUrl = picture,
                ),
                password = UUID.randomUUID().toString(),
            )

        val metadata =
            mutableMapOf<String, String>().apply {
                put(MetadataKeys.AUTH_TYPE, provider)
                put(MetadataKeys.EXTERNAL_ID, email)
                accessToken?.let { put(MetadataKeys.ACCESS_TOKEN, it) }
                put(MetadataKeys.ID_TOKEN, idToken)
            }

        linkExternalProviderUseCase.execute(userId = user.id, metadata = metadata)

        return generateAuthResponse(user)
    }

    private fun extractSocialUserInfo(
        idToken: String,
        provider: String,
        language: String,
    ): Triple<String, String, String?> {
        val payload = getPayloadFromIdToken(idToken)
        val jsonObject = json.parseToJsonElement(payload).jsonObject

        val email = jsonObject[SocialAuthConstants.CLAIM_EMAIL]?.jsonPrimitive?.content
        if (email == null) {
            throw AppException(
                AppError(
                    AppErrorCode.SOCIAL_AUTH_FAILED,
                    i18n.getString(AppStringType.ERROR_SOCIAL_EMAIL_NOT_FOUND, language),
                ),
            )
        }

        val firstName =
            jsonObject[SocialAuthConstants.CLAIM_GIVEN_NAME]?.jsonPrimitive?.content.orEmpty()
        val lastName =
            jsonObject[SocialAuthConstants.CLAIM_FAMILY_NAME]?.jsonPrimitive?.content.orEmpty()

        val name =
            jsonObject[SocialAuthConstants.CLAIM_NAME]?.jsonPrimitive?.content
                ?: "$firstName $lastName".trim().ifEmpty {
                    provider.replaceFirstChar { it.uppercase() } + SocialAuthConstants.DEFAULT_NAME_SUFFIX
                }
        val picture = jsonObject[SocialAuthConstants.CLAIM_PICTURE]?.jsonPrimitive?.content

        return Triple(email, name, picture)
    }

    private fun getPayloadFromIdToken(idToken: String): String {
        val parts = idToken.split(SocialAuthConstants.JWT_SEPARATOR)
        if (parts.size < 2) {
            throw AppException(AppError(AppErrorCode.SOCIAL_AUTH_FAILED, "Invalid JWT format"))
        }
        return String(Base64.getDecoder().decode(parts[1]))
    }

    suspend fun refreshToken(
        refreshToken: String,
        language: String,
    ): AuthResponse {
        val user = getValidatedUser(refreshToken, language)

        linkExternalProviderUseCase.execute(userId = user.id)

        return generateAuthResponse(user)
    }

    private suspend fun getValidatedUser(
        refreshToken: String,
        language: String,
    ): User {
        val claims =
            tokenService.verifyToken(refreshToken)
                ?: throw AppException(
                    AppError(
                        AppErrorCode.EXPIRED_TOKEN,
                        i18n.getString(AppStringType.ERROR_INVALID_REFRESH_TOKEN, language),
                    ),
                )

        return findUserByClaims(claims, language)
    }

    private suspend fun findUserByClaims(
        claims: Map<String, String?>,
        language: String,
    ): User {
        val userId =
            claims[TokenConstants.CLAIM_USER_ID]
                ?: throw AppException(
                    AppError(
                        AppErrorCode.INVALID_TOKEN,
                        i18n.getString(AppStringType.ERROR_INVALID_TOKEN_PAYLOAD, language),
                    ),
                )

        return userRepository.findById(userId)
            ?: throw AppException(
                AppError(
                    AppErrorCode.USER_NOT_FOUND,
                    i18n.getString(AppStringType.ERROR_USER_NOT_FOUND, language),
                ),
            )
    }

    private fun generateAuthResponse(user: User): AuthResponse =
        AuthResponse(
            user = user,
            accessToken = tokenService.generateAccessToken(user),
            refreshToken = tokenService.generateRefreshToken(user),
            challengeRequired = false,
        )
}
