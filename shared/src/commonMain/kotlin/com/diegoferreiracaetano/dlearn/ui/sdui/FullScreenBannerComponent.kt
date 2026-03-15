package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class FullScreenBannerComponent(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String
) : Component