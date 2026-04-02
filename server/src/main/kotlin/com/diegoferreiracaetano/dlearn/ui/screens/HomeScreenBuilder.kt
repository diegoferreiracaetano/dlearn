package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class HomeScreenBuilder(
    private val mapper: HomeMapper,
    private val i18n: I18nProvider,
) {
    fun build(
        data: HomeDomainData,
        lang: String,
        type: HomeFilterType = HomeFilterType.ALL,
    ): Screen {
        val components = mutableListOf<Component>()

        components.add(
            ChipGroupComponent(
                cleanUrl =
                    AppPath(
                        path = AppNavigationRoute.HOME,
                        params = mapOf(AppQueryParam.TYPE to HomeFilterType.ALL.name),
                    ),
                items =
                    listOf(
                        ChipItem(
                            id = HomeFilterType.SERIES.name,
                            label = i18n.getString(AppStringType.FILTER_SERIES, lang),
                            isSelected = type == HomeFilterType.SERIES,
                            actionUrl =
                                AppPath(
                                    path = AppNavigationRoute.HOME,
                                    params = mapOf(AppQueryParam.TYPE to HomeFilterType.SERIES.name),
                                ),
                        ),
                        ChipItem(
                            id = HomeFilterType.MOVIES.name,
                            label = i18n.getString(AppStringType.FILTER_MOVIES, lang),
                            isSelected = type == HomeFilterType.MOVIES,
                            actionUrl =
                                AppPath(
                                    path = AppNavigationRoute.HOME,
                                    params = mapOf(AppQueryParam.TYPE to HomeFilterType.MOVIES.name),
                                ),
                        ),
                    ),
            ),
        )
        data.banner?.let {
            components.add(mapper.toBannerMain(it))
        }

        if (data.top10.isNotEmpty()) {
            components.add(
                mapper.toCarousel(i18n.getString(AppStringType.SECTION_TOP_10, lang), data.top10, showRank = true),
            )
        }

        if (data.popular.isNotEmpty()) {
            components.add(mapper.toBannerCarousel(i18n.getString(AppStringType.SECTION_POPULAR, lang), data.popular))
        }

        data.categories.forEach { (categoryName, videos) ->
            if (videos.isNotEmpty()) {
                components.add(mapper.toCarousel(categoryName, videos))
            }
        }

        return Screen(
            components = components,
        )
    }
}
