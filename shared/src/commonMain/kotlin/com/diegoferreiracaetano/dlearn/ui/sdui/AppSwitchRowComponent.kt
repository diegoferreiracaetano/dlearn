package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppSwitchRowComponent(
    val title: String,
    val subtitle: String? = null,
    val icon: AppIconType? = null,
    val preferenceKey: String,
    val isChecked: Boolean = false,
) : Component
