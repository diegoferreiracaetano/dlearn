package com.diegoferreiracaetano.dlearn.model

import kotlinx.serialization.Serializable

@Serializable
data class TmdbAccountStatesRemote(
    val id: Int,
    val favorite: Boolean,
    val watchlist: Boolean
)
