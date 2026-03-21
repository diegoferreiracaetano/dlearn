package com.diegoferreiracaetano.dlearn.domain.password

import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeStatus
import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val userId: String,
    val newPassword: String
)

@Serializable
data class ChangePasswordResponse(
    val message: String,
    val status: ChallengeStatus = ChallengeStatus.SUCCESS
)
