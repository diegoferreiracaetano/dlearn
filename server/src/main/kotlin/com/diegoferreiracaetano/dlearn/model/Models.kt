package com.diegoferreiracaetano.dlearn.model

import com.diegoferreiracaetano.dlearn.domain.home.HomeCategory
import com.diegoferreiracaetano.dlearn.domain.home.HomeSectionType
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

// TMDB API DTOs (Internal to Server)
@Serializable
internal data class TmdbItemRemote(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    val overview: String = ""
)

internal fun TmdbItemRemote.toVideo(
    mediaType: MediaType,
    section: HomeSectionType? = null,
    category: HomeCategory? = null
) = Video(
    id = id.toString(),
    title = title ?: name ?: "",
    subtitle = "",
    description = overview,
    url = "",
    imageUrl = "https://image.tmdb.org/t/p/w500${posterPath.orEmpty()}",
    mediaType = mediaType,
    section = section,
    category = category
)

@Serializable
data class TmdbListResponse<T>(
    val results: List<T>
)

@Serializable
data class TmdbGenresResponse(
    val genres: List<HomeCategory>
)
