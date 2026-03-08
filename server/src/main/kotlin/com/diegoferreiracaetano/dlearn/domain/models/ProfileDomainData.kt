package com.diegoferreiracaetano.dlearn.domain.models

import kotlinx.serialization.Serializable

@Serializable
data class ProfileDomainData(
    val name: String,
    val email: String,
    val imageUrl: String,
    val isPremium: Boolean,
    val language: String,
    val country: String
)
