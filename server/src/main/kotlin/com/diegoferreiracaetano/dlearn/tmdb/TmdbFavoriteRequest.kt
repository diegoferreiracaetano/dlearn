package com.diegoferreiracaetano.dlearn.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbFavoriteRequest(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    @SerialName("favorite") val favorite: Boolean
)
