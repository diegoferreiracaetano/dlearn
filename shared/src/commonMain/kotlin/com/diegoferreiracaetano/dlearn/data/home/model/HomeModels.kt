package com.diegoferreiracaetano.dlearn.data.home.model

import kotlinx.serialization.Serializable

@Serializable
internal enum class SectionType {
    BANNER_MAIN,
    TOP_10,
    POPULAR,
    CATEGORY
}

@Serializable
internal data class LayoutSection(
    val type: SectionType,
    val dataKey: String,
    val title: String? = null
)

@Serializable
internal data class HomeData(
    val BANNER_MAIN: MovieRemote?,
    val TOP_10: List<MovieRemote>,
    val POPULAR: List<MovieRemote>,
    val CATEGORIES: List<CategoryItems>
)

@Serializable
internal data class Category(
    val id: Int,
    val name: String
)

@Serializable
internal data class CategoryItems(
    val category: Category,
    val items: List<MovieRemote>
)

@Serializable
internal data class HomeForClient(
    val layout: List<LayoutSection>,
    val data: HomeData
)
