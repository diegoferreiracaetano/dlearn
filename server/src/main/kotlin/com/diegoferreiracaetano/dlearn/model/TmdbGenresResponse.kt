package com.diegoferreiracaetano.dlearn.model

import kotlinx.serialization.Serializable

@Serializable
data class TmdbGenresResponse(
    val genres: List<TmdbGenre>,
)
