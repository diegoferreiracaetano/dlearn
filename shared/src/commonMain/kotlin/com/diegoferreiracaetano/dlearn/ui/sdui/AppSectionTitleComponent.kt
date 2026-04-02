package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppSectionTitleComponent(
    val title: String,
) : Component
