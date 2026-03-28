package com.diegoferreiracaetano.dlearn.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbSessionResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("session_id") val sessionId: String
)
