package com.diegoferreiracaetano.dlearn.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbRequestTokenResponse(
    @SerialName("success") val success: Boolean,
    @SerialName("expires_at") val expiresAt: String,
    @SerialName("request_token") val requestToken: String
)
