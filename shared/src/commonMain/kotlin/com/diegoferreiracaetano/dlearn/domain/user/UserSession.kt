package com.diegoferreiracaetano.dlearn.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class UserSession(
    val userId: String,
    val accessToken: String,
    val refreshToken: String,
    val providerSessions: List<ProviderSession> = emptyList(),
)
