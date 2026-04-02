package com.diegoferreiracaetano.dlearn.domain.video

import com.diegoferreiracaetano.dlearn.domain.home.HomeCategory
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val url: String,
    val imageUrl: String,
    val categories: List<VideoCategory> = listOf(),
    val isFavorite: Boolean = false,
    val rating: Float? = null,
    val progress: Float = 0f,
    val type: VideoType = VideoType.DEFAULT,
    val mediaType: MediaType = MediaType.MOVIES,
    val category: HomeCategory? = null,
)

@Serializable
enum class VideoType {
    BANNER,
    DEFAULT,
}

@Serializable
enum class MediaType {
    MOVIES,
    SERIES,
}
