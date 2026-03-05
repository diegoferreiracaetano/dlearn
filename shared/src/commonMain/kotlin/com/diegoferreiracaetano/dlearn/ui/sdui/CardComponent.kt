package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class CardComponent(
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val rank: Int? = null,
    val actionUrl: String? = null
) : Component
