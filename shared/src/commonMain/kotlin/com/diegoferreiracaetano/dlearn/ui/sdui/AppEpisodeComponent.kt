package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppEpisodeComponent(
    val id: String,
    val title: String,
    val description: String,
    val imageUrl: String,
    val duration: String,
    val isPremium: Boolean = false,
    val actionUrl: String? = null,
) : Component
