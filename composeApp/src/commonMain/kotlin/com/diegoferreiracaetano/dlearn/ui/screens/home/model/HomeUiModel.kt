package com.diegoferreiracaetano.dlearn.ui.screens.home.model
import com.diegoferreiracaetano.dlearn.domain.video.Video
import kotlinx.serialization.Serializable


enum class SectionType {
    BANNER_MAIN,
    TOP_10,
    POPULAR,
    CATEGORY
}

@Serializable
data class LayoutSection(
    val type: SectionType,
    val dataKey: String,
    val title: String? = null
)

@Serializable
data class HomeData(
    val BANNER_MAIN: Video?,
    val TOP_10: List<Video>,
    val POPULAR: List<Video>,
    val CATEGORIES: List<CategoryItems>
)

@Serializable
data class CategoryItems(
    val category: Category,
    val items: List<Video>
)

@Serializable
data class Category(
    val id: Int,
    val name: String
)

@Serializable
data class HomeForClient(
    val layout: List<LayoutSection>,
    val data: HomeData
)
