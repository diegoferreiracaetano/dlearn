package com.diegoferreiracaetano.dlearn.data.home.mapper

import com.diegoferreiracaetano.dlearn.data.home.model.HomeForClient
import com.diegoferreiracaetano.dlearn.data.home.model.SectionType
import com.diegoferreiracaetano.dlearn.domain.home.Home
import com.diegoferreiracaetano.dlearn.domain.home.HomeCategory
import com.diegoferreiracaetano.dlearn.domain.home.HomeCategoryItems
import com.diegoferreiracaetano.dlearn.domain.home.HomeDataContent
import com.diegoferreiracaetano.dlearn.domain.home.HomeLayoutSection
import com.diegoferreiracaetano.dlearn.domain.home.HomeSectionType

internal fun HomeForClient.toDomain(): Home {
    val layoutSection = layout.map {
        HomeLayoutSection(
            type = when (it.type) {
                SectionType.BANNER_MAIN -> HomeSectionType.BANNER_MAIN
                SectionType.TOP_10 -> HomeSectionType.TOP_10
                SectionType.POPULAR -> HomeSectionType.POPULAR
                SectionType.CATEGORY -> HomeSectionType.CATEGORY
            },
            title = it.title
        )
    }

    val homeData = HomeDataContent(
        bannerMain = data.BANNER_MAIN?.toVideo(),
        top10 = data.TOP_10.map { it.toVideo() },
        popular = data.POPULAR.map { it.toVideo() },
        categories = data.CATEGORIES.map { categoryItems ->
            HomeCategoryItems(
                category = HomeCategory(categoryItems.category.id, categoryItems.category.name),
                items = categoryItems.items.map { it.toVideo() }
            )
        }
    )

    return Home(layoutSection, homeData)
}
