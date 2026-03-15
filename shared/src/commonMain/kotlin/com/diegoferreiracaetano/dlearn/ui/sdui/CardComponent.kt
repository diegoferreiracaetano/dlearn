package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class CardComponent(
    val id: String,
    val title: String,
    val subtitle: String? = null,
    val imageUrl: String,
    val rating: String? = null,
    val year: String? = null,
    val duration: String? = null,
    val contentRating: String? = null,
    val genre: String? = null,
    val movieType: String? = null,
    val isPremium: Boolean = false,
    val rank: Int? = null,
    val actionUrl: String? = null
) : Component
