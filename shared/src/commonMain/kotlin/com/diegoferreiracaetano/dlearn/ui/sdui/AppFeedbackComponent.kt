package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppFeedbackComponent(
    val title: String,
    val description: String,
    val imageSource: String? = null,
    val primaryText: String? = null,
    val secondaryText: String? = null
) : Component
