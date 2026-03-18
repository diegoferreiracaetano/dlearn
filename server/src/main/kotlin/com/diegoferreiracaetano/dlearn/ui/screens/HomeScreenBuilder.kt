package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.HomeFilterIds
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class HomeScreenBuilder(
    private val mapper: HomeMapper,
    private val i18n: I18nProvider
) {
    fun build(
        data: HomeDomainData,
        appVersion: Int,
        lang: String,
        type: HomeFilterType = HomeFilterType.ALL
    ): Screen {
        val components = mutableListOf<Component>()

        components.add(
            ChipGroupComponent(
                items = listOf(
                    ChipItem(
                        id = HomeFilterIds.SERIES,
                        label = i18n.getString(AppStringType.FILTER_SERIES, lang),
                        isSelected = type == HomeFilterType.SERIES
                    ),
                    ChipItem(
                        id = HomeFilterIds.MOVIES,
                        label = i18n.getString(AppStringType.FILTER_MOVIES, lang),
                        isSelected = type == HomeFilterType.MOVIE
                    )
                )
            )
        )

        data.banner?.let {
            components.add(mapper.toBannerMain(it))
        }

        if (data.top10.isNotEmpty()) {
            components.add(mapper.toCarousel(i18n.getString(AppStringType.SECTION_TOP_10, lang), data.top10, showRank = true))
        }

        if (data.popular.isNotEmpty()) {
            components.add(mapper.toBannerCarousel(i18n.getString(AppStringType.SECTION_POPULAR, lang), data.popular))
        }

        data.categories.forEach { (categoryName, videos) ->
            if (videos.isNotEmpty()) {
                components.add(mapper.toCarousel(categoryName, videos))
            }
        }

        val container = AppContainerComponent(
            topBar = AppTopBarComponent(
                title = i18n.getString(AppStringType.HOME_TITLE, lang),
                subtitle = i18n.getString(AppStringType.HOME_SUBTITLE, lang),
                imageUrl = AppConstants.AVATAR_PLACEHOLDER,
                showSearch = true
            ),
            components = components
        )

        return Screen(
            components = listOf(container)
        )
    }
}
