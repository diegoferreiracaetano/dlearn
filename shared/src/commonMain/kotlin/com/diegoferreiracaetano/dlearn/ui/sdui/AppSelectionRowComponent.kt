package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppSelectionRowComponent(
    val title: String,
    val subtitle: String? = null,
    val preferenceKey: String,
    val value: String,
    val isSelected: Boolean = false
) : Component
