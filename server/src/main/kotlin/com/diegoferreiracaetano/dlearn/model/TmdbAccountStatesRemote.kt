package com.diegoferreiracaetano.dlearn.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbAccountStatesRemote(
    val id: Int,
    val favorite: Boolean = false,
    val watchlist: Boolean = false
)

@Serializable
data class TmdbStatusResponse(
    @SerialName("status_code") val statusCode: Int,
    @SerialName("status_message") val statusMessage: String,
    val success: Boolean = false
)
