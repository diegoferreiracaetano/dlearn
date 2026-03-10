package com.diegoferreiracaetano.dlearn.model

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
    val videos: TmdbVideosResponseRemote? = null
)

@Serializable
data class TmdbCreditsRemote(
    val cast: List<TmdbCastRemote> = emptyList()
)

@Serializable
data class TmdbCastRemote(
    val id: Int,
    val name: String,
    val character: String,
    @SerialName("profile_path") val profilePath: String? = null
)

@Serializable
data class TmdbVideosResponseRemote(
    val results: List<TmdbVideoRemote> = emptyList()
)

@Serializable
data class TmdbVideoRemote(
    val id: String,
    val key: String,
    val name: String,
    val site: String,
    val type: String
)
