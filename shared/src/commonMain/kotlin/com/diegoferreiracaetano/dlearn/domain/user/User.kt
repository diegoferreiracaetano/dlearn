package com.diegoferreiracaetano.dlearn.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val email: String,
    val password: String? = null,
    val imageUrl: String? = null,
    val isPremium: Boolean = false,
    val language: String? = null,
    val country: String? = null,
    val phoneNumber: String? = null
)
