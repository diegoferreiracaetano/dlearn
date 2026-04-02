package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class MovieCarouselComponent(
    val title: String,
    val items: List<MovieItemComponent>,
) : Component
