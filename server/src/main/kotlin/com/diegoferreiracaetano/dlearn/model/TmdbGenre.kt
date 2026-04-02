package com.diegoferreiracaetano.dlearn.model

import kotlinx.serialization.Serializable

@Serializable
data class TmdbGenre(
    val id: Int,
    val name: String,
)
