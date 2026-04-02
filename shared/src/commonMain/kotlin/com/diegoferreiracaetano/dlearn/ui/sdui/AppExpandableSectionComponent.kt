package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppExpandableSectionComponent(
    val title: String,
    val text: String,
    val maxLines: Int = 3,
) : Component
