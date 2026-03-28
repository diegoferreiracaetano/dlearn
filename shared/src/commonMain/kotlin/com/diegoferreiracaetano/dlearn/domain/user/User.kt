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
    val phoneNumber: String? = null
)

interface Provider {
    val tmdb: Tmdb?
    val tmdbSessionId: String?
    val tmdbAccountId: String?
}

@Serializable
data class MovieProvider(
    override val tmdb: Tmdb? = null
) : Provider {
    override val tmdbSessionId: String? get() = tmdb?.sessionId
    override val tmdbAccountId: String? get() = tmdb?.accountId
}

@Serializable
data class Tmdb(
    val sessionId: String?,
    val accountId: String?
)
