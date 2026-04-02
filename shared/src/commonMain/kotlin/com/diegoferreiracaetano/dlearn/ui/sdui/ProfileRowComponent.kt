package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class ProfileRowComponent(
    val name: String,
    val email: String,
    val imageUrl: String? = null,
    val editActionUrl: String? = null,
) : Component
