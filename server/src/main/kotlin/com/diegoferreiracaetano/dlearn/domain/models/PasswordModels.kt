package com.diegoferreiracaetano.dlearn.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ChangePasswordRequest(
    val newPassword: String
)

@Serializable
data class ChangePasswordResponse(
    val message: String,
    val status: ChallengeStatus = ChallengeStatus.SUCCESS
)
