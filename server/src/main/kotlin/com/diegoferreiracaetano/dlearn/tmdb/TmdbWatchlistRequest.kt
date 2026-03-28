package com.diegoferreiracaetano.dlearn.tmdb

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbWatchlistRequest(
    @SerialName("media_type") val mediaType: String,
    @SerialName("media_id") val mediaId: Int,
    @SerialName("watchlist") val watchlist: Boolean
)
