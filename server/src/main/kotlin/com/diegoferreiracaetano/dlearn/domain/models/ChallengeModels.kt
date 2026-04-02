package com.diegoferreiracaetano.dlearn.domain.models

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
