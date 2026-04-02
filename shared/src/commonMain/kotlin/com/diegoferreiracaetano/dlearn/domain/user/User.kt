package com.diegoferreiracaetano.dlearn.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val imageUrl: String? = null,
    val isPremium: Boolean = false,
    val phoneNumber: String? = null,
)
