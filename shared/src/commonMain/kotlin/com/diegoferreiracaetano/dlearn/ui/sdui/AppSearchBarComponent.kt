package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppSearchBarComponent(
    val query: String = "",
    val placeholder: String = "",
    val components: List<Component> = emptyList()
) : Component
