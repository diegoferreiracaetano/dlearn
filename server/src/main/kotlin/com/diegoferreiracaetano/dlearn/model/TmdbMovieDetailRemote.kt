package com.diegoferreiracaetano.dlearn.model

import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TmdbMovieDetailRemote(
    val id: Int,
    val title: String? = null,
    val name: String? = null,
    @SerialName("poster_path") val posterPath: String? = null,
    @SerialName("backdrop_path") val backdropPath: String? = null,
    val overview: String? = null,
    @SerialName("release_date") val releaseDate: String? = null,
    @SerialName("first_air_date") val firstAirDate: String? = null,
    val runtime: Int? = null,
    @SerialName("vote_average") val voteAverage: Double? = null,
    val genres: List<TmdbGenre> = emptyList(),
    val credits: TmdbCreditsRemote? = null,
    val videos: TmdbVideosResponseRemote? = null,
    @SerialName("external_ids") val externalIds: TmdbExternalIdsRemote? = null,
    @SerialName("watch/providers") val watchProviders: TmdbWatchProvidersResponse? = null,
)

fun TmdbMovieDetailRemote.toVideo(mediaType: MediaType): Video =
    Video(
        id = id.toString(),
        title = title ?: name ?: "",
        subtitle = (releaseDate ?: firstAirDate ?: "").take(4),
        description = overview ?: "",
        url = "",
        imageUrl = "${TmdbConstants.IMAGE_BASE_URL}${TmdbConstants.IMAGE_W500}$posterPath",
        rating = voteAverage?.toFloat(),
        mediaType = mediaType,
        categories = genres.map { VideoCategory(id = it.id.toString(), title = it.name) },
    )

@Serializable
data class TmdbExternalIdsRemote(
    @SerialName("imdb_id") val imdbId: String? = null,
)

@Serializable
data class TmdbCreditsRemote(
    val cast: List<TmdbCastRemote> = emptyList(),
)

@Serializable
data class TmdbCastRemote(
    val id: Int,
    val name: String,
    val character: String,
    @SerialName("profile_path") val profilePath: String? = null,
)

@Serializable
data class TmdbVideosResponseRemote(
    val results: List<TmdbVideoRemote> = emptyList(),
)

@Serializable
data class TmdbVideoRemote(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String,
)
