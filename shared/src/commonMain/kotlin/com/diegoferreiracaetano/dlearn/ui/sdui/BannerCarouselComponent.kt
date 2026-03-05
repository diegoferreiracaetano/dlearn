package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("BannerCarouselComponent")
data class BannerCarouselComponent(
    val title: String,
    val items: List<CardComponent>
) : Component
