package com.diegoferreiracaetano.dlearn.model

import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
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
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("vote_average") val voteAverage: Float? = null,
    @SerialName("genre_ids") val genreIds: List<Int>? = null,
    @SerialName("media_type") val mediaType: String? = null,
)

fun TmdbItemRemote.toVideo(
    fallbackMediaType: MediaType,
    allGenres: List<TmdbGenre> = emptyList(),
    isFavorite: Boolean = false,
): Video {
    val categories =
        genreIds?.mapNotNull { id ->
            allGenres.find { it.id == id }?.let {
                VideoCategory(id = it.id.toString(), title = it.name)
            }
        } ?: emptyList()

    val type =
        when (mediaType) {
            "movie" -> MediaType.MOVIES
            "tv" -> MediaType.SERIES
            else -> fallbackMediaType
        }

    return Video(
        id = "${type.name}_$id",
        title = title ?: name ?: "",
        subtitle = (releaseDate ?: firstAirDate ?: "").take(4),
        description = overview ?: "",
        url = "",
        imageUrl = "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W500}$posterPath",
        rating = voteAverage,
        mediaType = type,
        categories = categories,
        isFavorite = isFavorite,
    )
}
