package com.diegoferreiracaetano.dlearn.model

import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbItemRemote(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("overview") val overview: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    @SerialName("release_date") val releaseDate: String? = null
)

fun TmdbItemRemote.toVideo(mediaType: MediaType): Video {
    return Video(
        id = id.toString(),
        title = title ?: name ?: "",
        subtitle = (releaseDate ?: firstAirDate ?: "").take(4),
        description = overview ?: "",
        url = "",
        imageUrl = "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W500}$posterPath",
        mediaType = mediaType
    )
}
