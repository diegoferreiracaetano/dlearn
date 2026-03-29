package com.diegoferreiracaetano.dlearn.domain.user

import kotlinx.serialization.Serializable

@Serializable
data class Tmdb(
    val sessionId: String?,
    val accountId: String?
)
