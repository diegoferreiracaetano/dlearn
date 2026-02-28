package com.diegoferreiracaetano.dlearn.domain.home

import com.diegoferreiracaetano.dlearn.domain.video.Video
import kotlinx.serialization.Serializable

@Serializable
enum class HomeFilterType {
    ALL,
    MOVIE,
    SERIES
}

// Public Enum for the UI Layer
@Serializable
enum class HomeSectionType {
    BANNER_MAIN,
    TOP_10,
    POPULAR,
    CATEGORY
}

@Serializable
data class HomeCategory(
    val id: Int,
    val name: String
)

@Serializable
data class HomeDataContent(
    val items: List<Video>
)

@Serializable
data class Home(
    val sections: List<HomeSectionType>,
    val data: HomeDataContent
)
