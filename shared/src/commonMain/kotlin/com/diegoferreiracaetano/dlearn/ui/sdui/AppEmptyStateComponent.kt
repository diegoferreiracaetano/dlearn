package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppEmptyStateComponent(
    val title: String,
    val description: String,
    val image: AppImageType? = null
) : Component
