package com.diegoferreiracaetano.dlearn.domain.auth

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val name: String,
    val email: String,
    val password: String,
    val tmdbUsername: String? = null,
    val tmdbPassword: String? = null
)
