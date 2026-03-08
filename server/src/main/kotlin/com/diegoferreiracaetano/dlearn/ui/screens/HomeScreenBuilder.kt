package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class HomeScreenBuilder(
    private val mapper: HomeMapper,
    private val i18n: I18nProvider
) {
    fun build(data: HomeDomainData, appVersion: Int, lang: String): Screen {
        val components = mutableListOf<Component>()

        // Adicionando Chips de Filtro
        components.add(
            ChipGroupComponent(
                id = "filters",
                items = listOf(
                    ChipItem(id = "series", label = i18n.getString(AppStringType.FILTER_SERIES, lang)),
                    ChipItem(id = "movies", label = i18n.getString(AppStringType.FILTER_MOVIES, lang)),
                    ChipItem(id = "categories", label = i18n.getString(AppStringType.FILTER_CATEGORIES, lang))
                )
            )
        )

        // Banner Principal
        data.banner?.let {
            components.add(mapper.toBannerMain(it))
        }

        // Seção Top 10
        if (data.top10.isNotEmpty()) {
            components.add(mapper.toCarousel(i18n.getString(AppStringType.SECTION_TOP_10, lang), data.top10, showRank = true))
        }

        // Seção Populares
        if (data.popular.isNotEmpty()) {
            components.add(mapper.toBannerCarousel(i18n.getString(AppStringType.SECTION_POPULAR, lang), data.popular))
        }

        // Categorias Dinâmicas
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
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_HOME, lang), route = "home", icon = AppIconType.INFO),
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_SEARCH, lang), route = "search", icon = AppIconType.HELP),
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_FAVORITES, lang), route = "favorite", icon = AppIconType.NOTIFICATIONS),
                    BottomNavItem(label = i18n.getString(AppStringType.NAV_PROFILE, lang), route = "person", icon = AppIconType.PERSON)
                ),
                selectedRoute = "home"
            ),
            components = components
        )

        return Screen(
            id = "home",
            components = listOf(container)
        )
    }
}
