package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class Screen(
    val components: List<Component> = emptyList(),
)
