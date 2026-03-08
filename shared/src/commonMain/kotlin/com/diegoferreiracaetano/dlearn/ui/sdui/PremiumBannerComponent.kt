package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class PremiumBannerComponent(
    val title: String,
    val description: String,
    val icon: AppIconType? = null,
    val actionUrl: String? = null
) : Component
