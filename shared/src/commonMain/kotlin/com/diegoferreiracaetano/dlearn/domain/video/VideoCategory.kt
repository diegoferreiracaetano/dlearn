package com.diegoferreiracaetano.dlearn.domain.video

import kotlinx.serialization.Serializable

@Serializable
data class VideoCategory(
    val id: String,
    val title: String
)
