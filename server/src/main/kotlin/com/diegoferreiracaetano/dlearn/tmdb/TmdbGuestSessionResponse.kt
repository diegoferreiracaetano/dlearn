package com.diegoferreiracaetano.dlearn.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbGuestSessionResponse(
    val success: Boolean,
    @SerialName("guest_session_id")
    val guestSessionId: String? = null,
    @SerialName("expires_at")
    val expiresAt: String? = null,
    @SerialName("status_message")
    val statusMessage: String? = null
)
