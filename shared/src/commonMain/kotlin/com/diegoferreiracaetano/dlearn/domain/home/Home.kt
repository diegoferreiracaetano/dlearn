package com.diegoferreiracaetano.dlearn.domain.home

import com.diegoferreiracaetano.dlearn.domain.video.Video

// Public Enum for the UI Layer
enum class HomeSectionType {
    BANNER_MAIN,
    TOP_10,
    POPULAR,
    CATEGORY
}

// Public Models for the UI Layer
data class HomeLayoutSection(
    val type: HomeSectionType,
    val title: String? = null
)

data class HomeCategory(
    val id: Int,
    val name: String
)

data class HomeCategoryItems(
    val category: HomeCategory,
    val items: List<Video>
)

data class HomeDataContent(
    val bannerMain: Video?,
    val top10: List<Video>,
    val popular: List<Video>,
    val categories: List<HomeCategoryItems>
)

data class Home(
    val layout: List<HomeLayoutSection>,
    val data: HomeDataContent
)
