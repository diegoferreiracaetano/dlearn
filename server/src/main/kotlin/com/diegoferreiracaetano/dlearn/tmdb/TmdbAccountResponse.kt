package com.diegoferreiracaetano.dlearn.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbAccountResponse(
    @SerialName("id") val id: Int,
    @SerialName("username") val username: String
)
