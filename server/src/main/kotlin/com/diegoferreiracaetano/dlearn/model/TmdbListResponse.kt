package com.diegoferreiracaetano.dlearn.model

import kotlinx.serialization.Serializable

@Serializable
data class TmdbListResponse<T>(
    val results: List<T>
)
