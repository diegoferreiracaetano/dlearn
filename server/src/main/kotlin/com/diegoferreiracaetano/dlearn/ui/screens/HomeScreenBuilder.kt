package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ComponentIds
import com.diegoferreiracaetano.dlearn.HomeFilterIds
import com.diegoferreiracaetano.dlearn.NavigationRoutes
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
        type: HomeFilterType = HomeFilterType.ALL,
        categoryId: String? = null
    ): Screen {
        val components = mutableListOf<Component>()

        val categoriesOptions = data.categories.keys.map { categoryName ->
            val id = categoryName.lowercase().replace(" ", "_")
            ChipItem(
                id = id,
                label = categoryName,
                isFilter = true,
                isSelected = categoryId == id
            )
        }

        val isCategorySelected = categoryId != null
        val categoryLabel = if (isCategorySelected) {
            data.categories.keys.firstOrNull { it.lowercase().replace(" ", "_") == categoryId }
                ?: i18n.getString(AppStringType.FILTER_CATEGORIES, lang)
        } else {
            i18n.getString(AppStringType.FILTER_CATEGORIES, lang)
        }

        components.add(
            ChipGroupComponent(
                id = ComponentIds.FILTERS,
                items = listOf(
                    ChipItem(
                        id = HomeFilterIds.SERIES,
                        label = i18n.getString(AppStringType.FILTER_SERIES, lang),
                        isSelected = type == HomeFilterType.SERIES && !isCategorySelected
                    ),
                    ChipItem(
                        id = HomeFilterIds.MOVIES,
                        label = i18n.getString(AppStringType.FILTER_MOVIES, lang),
                        isSelected = type == HomeFilterType.MOVIE && !isCategorySelected
                    ),
                    ChipItem(
                        id = HomeFilterIds.CATEGORIES,
                        label = categoryLabel,
                        hasDropDown = true,
                        isFilter = false,
                        isSelected = isCategorySelected,
                        options = categoriesOptions
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
                imageUrl = "https://avatars.githubusercontent.com/u/1023?v=4",
                showSearch = true
            ),
            bottomBar = BottomNavigationComponent(
                items = listOf(
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_HOME, lang), route = NavigationRoutes.HOME, icon = AppIconType.INFO),
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_SEARCH, lang), route = NavigationRoutes.SEARCH, icon = AppIconType.HELP),
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_FAVORITES, lang), route = NavigationRoutes.FAVORITE, icon = AppIconType.NOTIFICATIONS),
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_PROFILE, lang), route = NavigationRoutes.PROFILE, icon = AppIconType.PERSON)
                ),
                selectedRoute = NavigationRoutes.HOME
            ),
            components = components
        )

        return Screen(
            id = ComponentIds.HOME_SCREEN,
            components = listOf(container)
        )
    }
}
