package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class AppProfileHeaderComponent(
    val name: String,
    val email: String,
    val imageUrl: String? = null,
    val onImagePickedAction: String? = null,
) : Component
