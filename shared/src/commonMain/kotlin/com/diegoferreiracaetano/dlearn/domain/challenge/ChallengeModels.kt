package com.diegoferreiracaetano.dlearn.domain.challenge

import kotlinx.serialization.Serializable

@Serializable
enum class ChallengeType {
    OTP_SMS,
    OTP_EMAIL,
    BIOMETRIC,
    DEVICE_BINDING,
    UNKNOWN
}

@Serializable
data class Challenge(
    val challengeType: ChallengeType,
    val data: Map<String, String> = emptyMap()
)

@Serializable
data class ChallengeSession(
    val transactionId: String,
    val challenges: List<Challenge>,
    val expiresAt: Long? = null
)

sealed interface ChallengeResult {
    data class Success(val data: Map<String, String>) : ChallengeResult
    data object Cancelled : ChallengeResult
    data class Failure(val error: Throwable) : ChallengeResult
}
