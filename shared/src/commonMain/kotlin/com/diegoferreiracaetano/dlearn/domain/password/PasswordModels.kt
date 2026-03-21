package com.diegoferreiracaetano.dlearn.domain.password

import kotlinx.serialization.Serializable

@Serializable
enum class PasswordStatus {
    SUCCESS,
    CHALLENGE_REQUIRED,
    ERROR
}

@Serializable
enum class PasswordChallengeCode {
    OTP_REQUIRED
}

@Serializable
data class ChangePasswordRequest(
    val userId: String,
    val newPassword: String
)

@Serializable
data class ChangePasswordResponse(
    val message: String,
    val status: PasswordStatus = PasswordStatus.SUCCESS
)

@Serializable
data class PasswordChallengeError(
    val code: PasswordChallengeCode,
    val message: String,
    val challengeToken: String
)

@Serializable
data class VerifyOtpRequest(
    val userId: String,
    val otpCode: String
)

@Serializable
data class VerifyOtpResponse(
    val success: Boolean,
    val message: String,
    val validatedToken: String? = null
)

class PasswordChallengeException(val error: PasswordChallengeError) : Exception(error.message)
