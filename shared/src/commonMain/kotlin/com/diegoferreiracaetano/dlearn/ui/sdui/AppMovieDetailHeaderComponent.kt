package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppMovieDetailHeaderComponent(
    val title: String,
    val imageUrl: String,
    val year: String? = null,
    val duration: String? = null,
    val genre: String? = null,
    val rating: Double? = null,
    val trailerId: String? = null,
    val downloadActionUrl: String? = null,
    val shareActionUrl: String? = null
) : Component
