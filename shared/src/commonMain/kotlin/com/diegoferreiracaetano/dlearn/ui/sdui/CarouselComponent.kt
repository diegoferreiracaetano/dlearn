package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class CarouselComponent(
    val title: String,
    val items: List<CardComponent>
) : Component
