package com.diegoferreiracaetano.dlearn.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class AuthRequest(
    val email: String,
    val password: String,
    val metadata: Map<String, String> = emptyMap()
)
