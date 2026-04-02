package com.diegoferreiracaetano.dlearn.domain.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import kotlinx.serialization.Serializable

@Serializable
enum class ChallengeStatus {
    SUCCESS,
    CHALLENGE_REQUIRED,
    ERROR,
}

@Serializable
enum class ChallengeCode {
    CHALLENGE_REQUIRED,
}

@Serializable
data class ChallengeError(
    val code: ChallengeCode,
    val message: String,
    val challengeToken: String,
)

@Serializable
data class ResolveChallengeRequest(
    val transactionId: String,
    val type: ChallengeType,
    val answers: Map<String, String>,
)

@Serializable
data class ResolveChallengeResponse(
    val validatedToken: String? = null,
    val success: Boolean = true,
    val message: String? = null,
)

class ChallengeException(
    val error: ChallengeError,
) : Exception(error.message)
