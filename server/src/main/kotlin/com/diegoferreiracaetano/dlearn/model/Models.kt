package com.diegoferreiracaetano.dlearn.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

enum class SectionType {
    BANNER_MAIN,
    TOP_10,
    POPULAR,
    CATEGORY
}

@Serializable
data class Movie(
    val id: Int,
    val title: String,
    @SerialName("poster_path") val posterPath: String?,
    @SerialName("backdrop_path") val backdropPath: String?,
    val overview: String
)

@Serializable
data class Category(
    val id: Int,
    val name: String
)

@Serializable
data class CategoryItems(
    val category: Category,
    val items: List<Movie>
)

@Serializable
data class LayoutSection(
    val type: SectionType,
    val dataKey: String,
    val title: String? = null
)

@Serializable
data class HomeData(
    @SerialName("BANNER_MAIN") val bannerMain: Movie?,
    @SerialName("TOP_10") val top10: List<Movie>,
    @SerialName("POPULAR") val popular: List<Movie>,
    @SerialName("CATEGORIES") val categories: List<CategoryItems>
)

@Serializable
data class HomeForClient(
    val layout: List<LayoutSection>,
    val data: HomeData
)

// TMDB API DTOs
@Serializable
data class TmdbListResponse<T>(
    val results: List<T>
)

@Serializable
data class TmdbGenresResponse(
    val genres: List<Category>
)
