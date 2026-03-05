package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class BannerComponent(
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val actionUrl: String? = null
) : Component
