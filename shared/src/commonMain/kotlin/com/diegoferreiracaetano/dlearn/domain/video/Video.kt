package com.diegoferreiracaetano.dlearn.domain.video

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
    val rating: Float = 0f,
    val progress: Float = 0f,
    val type: VideoType = VideoType.DEFAULT,
    val mediaType: MediaType = MediaType.MOVIE,
    val section: com.diegoferreiracaetano.dlearn.domain.home.HomeSectionType? = null,
    val category: com.diegoferreiracaetano.dlearn.domain.home.HomeCategory? = null
)

@Serializable
enum class VideoType {
    BANNER,
    DEFAULT
}

@Serializable
enum class MediaType {
    MOVIE,
    SERIES
}
