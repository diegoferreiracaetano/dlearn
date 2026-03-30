package com.diegoferreiracaetano.dlearn.domain.error

import kotlinx.serialization.Serializable

@Serializable
enum class AppErrorCode {
    // Generic Errors
    UNKNOWN_ERROR,
    NETWORK_ERROR,
    SERVER_ERROR,
    BAD_REQUEST,
    UNAUTHORIZED,
    FORBIDDEN,
    NOT_FOUND,
    TIMEOUT,

    SERVICE_UNAVAILABLE,

    // Auth Errors
    INVALID_CREDENTIALS,
    USER_NOT_FOUND,
    USER_ALREADY_EXISTS,
    INVALID_TOKEN,
    EXPIRED_TOKEN,
    EMAIL_ALREADY_IN_USE,

    // Social Auth Errors
    SOCIAL_AUTH_CONFIG_MISSING,
    UNSUPPORTED_CREDENTIAL_TYPE,
    SOCIAL_AUTH_FAILED,
    SOCIAL_AUTH_CANCELED,

    // Validation Errors
    VALIDATION_FAILED,
    FIELD_REQUIRED,
    INVALID_FORMAT,

    // Challenge Errors
    CHALLENGE_REQUIRED,
    INVALID_CHALLENGE_CODE,
    CHALLENGE_EXPIRED,
    TRANSACTION_ID_REQUIRED,

    // Specific Domain Errors
    MOVIE_NOT_FOUND,
    PAYMENT_FAILED,
    INSUFFICIENT_FUNDS
}

@Serializable
data class AppError(
    val code: AppErrorCode,
    val message: String? = null,
    val details: Map<String, String>? = null
)

class AppException(
    val error: AppError,
    cause: Throwable? = null
) : Exception(error.message ?: error.code.name, cause)
