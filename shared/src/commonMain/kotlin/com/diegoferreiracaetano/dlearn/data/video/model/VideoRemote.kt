package com.diegoferreiracaetano.dlearn.data.video.model

import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import com.diegoferreiracaetano.dlearn.domain.video.VideoType
import kotlinx.serialization.Serializable

@Serializable
internal data class VideoResponse(val results: List<VideoRemote>)

@Serializable
internal data class VideoRemote(
    val id: String,
    val title: String,
    val subtitle: String?,
    val description: String?,
    val url: String?,
    val imageUrl: String?,
    val categories: List<VideoCategory>? = listOf(),
    val isFavorite: Boolean? = false,
    val rating: Float? = 0f,
    val progress: Float? = 0f,
    val type: VideoType? = VideoType.DEFAULT
)
