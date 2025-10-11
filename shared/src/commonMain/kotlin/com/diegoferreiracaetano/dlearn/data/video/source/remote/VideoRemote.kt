package com.diegoferreiracaetano.dlearn.data.video.source.remote

import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import com.diegoferreiracaetano.dlearn.domain.video.VideoType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoRemote(
    @SerialName("id") val id: Long,
    @SerialName("title") val title: String,
    @SerialName("overview") val overview: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    @SerialName("release_date") val releaseDate: String?,
    @SerialName("vote_average") val voteAverage: Double?,
)

fun VideoRemote.toDomain(
    isBanner: Boolean = false,
    isFavorite: Boolean = false,
    progress: Float = 0f,
) = Video(
    id = id.toString(),
    title = title,
    subtitle = releaseDate ?: "",
    type = if (isBanner) VideoType.BANNER else VideoType.DEFAULT,
    categories = listOf(VideoCategory.PERFORMANCE),
    description = overview,
    url = "",
    imageUrl = "https://image.tmdb.org/t/p/w500${posterPath.orEmpty()}",
    rating = voteAverage?.toFloat() ?: 0f,
    isFavorite = isFavorite,
    progress = progress,
)

fun List<VideoRemote>.toDomain() = mapIndexed { index, videoRemote ->
    videoRemote.toDomain(
        isBanner = index == 0,
        isFavorite = index == 1 || index == 2,
        progress = if (index == 3 || index == 4) 0.10f else 0f
    )
}
