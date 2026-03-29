package com.diegoferreiracaetano.dlearn.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbRequestTokenResponse(
    @SerialName("success") val success: Boolean = false,
    @SerialName("expires_at") val expiresAt: String? = null,
    @SerialName("request_token") val requestToken: String? = null,
    @SerialName("status_message") val statusMessage: String? = null,
    @SerialName("status_code") val statusCode: Int? = null
)
