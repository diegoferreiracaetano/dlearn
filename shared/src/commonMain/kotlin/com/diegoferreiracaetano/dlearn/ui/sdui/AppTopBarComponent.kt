package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppTopBarComponent(
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String? = null,
    val showSearch: Boolean = false
) : Component
