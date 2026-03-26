package com.diegoferreiracaetano.dlearn.domain.home

import kotlinx.serialization.Serializable

@Serializable
enum class HomeFilterType {
    ALL,
    MOVIES,
    SERIES


}

@Serializable
data class HomeCategory(
    val id: Int,
    val name: String
)
