package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@SerialName("CarouselComponent")
data class CarouselComponent(
    val title: String? = null,
    val items: List<Component>
) : Component
