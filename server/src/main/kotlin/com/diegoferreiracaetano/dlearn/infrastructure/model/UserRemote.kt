package com.diegoferreiracaetano.dlearn.infrastructure.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRemote(
    val id: String,
    val name: String,
    val email: String,
    val password: String? = null,
    val imageUrl: String? = null,
    val isPremium: Boolean = false,
    val phoneNumber: String? = null
)
