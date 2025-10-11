package com.diegoferreiracaetano.dlearn.domain.video

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
    val type: VideoType = VideoType.DEFAULT
)

enum class VideoType {
    BANNER,
    DEFAULT
}