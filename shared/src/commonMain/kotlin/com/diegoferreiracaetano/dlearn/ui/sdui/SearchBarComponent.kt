package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class SearchBarComponent(
    val query: String = "",
    val placeholder: String? = null,
    val active: Boolean = false
) : Component
