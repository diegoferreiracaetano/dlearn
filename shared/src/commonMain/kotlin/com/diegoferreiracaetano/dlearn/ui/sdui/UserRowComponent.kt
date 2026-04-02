package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class UserRowComponent(
    val name: String,
    val role: String,
    val imageUrl: String? = null,
    val actionUrl: String? = null,
) : Component
