package com.diegoferreiracaetano.dlearn.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class AuthProvider(
    val provider: AccountProvider,
    val externalId: String,
    val metadata: Map<String, String> = emptyMap(),
)
