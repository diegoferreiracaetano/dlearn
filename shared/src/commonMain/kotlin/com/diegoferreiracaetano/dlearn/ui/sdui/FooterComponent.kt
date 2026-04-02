package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class FooterComponent(
    val label: String,
    val actionUrl: String? = null,
    val closeUrl: String? = null,
) : Component
