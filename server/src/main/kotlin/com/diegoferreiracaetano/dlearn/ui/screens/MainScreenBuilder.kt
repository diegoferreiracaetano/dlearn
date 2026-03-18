package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.HomeFilterIds
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMainContentComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipItem
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class MainScreenBuilder(
    private val i18n: I18nProvider
) {
    fun build(
        userId: String,
        appVersion: Int,
        lang: String
    ): Screen {
        val bottomBar = BottomNavigationComponent(
            items = listOf(
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_HOME, lang),
                    route = NavigationRoutes.HOME,
                    icon = AppIconType.HOME
                ),
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_WATCHLIST, lang),
                    route = NavigationRoutes.buildRoute(NavigationRoutes.WATCHLIST),
                    icon = AppIconType.WATCHLIST
                ),
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_FAVORITES, lang),
                    route = NavigationRoutes.buildRoute(NavigationRoutes.FAVORITE),
                    icon = AppIconType.FAVORITE
                ),
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_PROFILE, lang),
                    route = NavigationRoutes.PROFILE,
                    icon = AppIconType.PERSON
                )
            ),
            selectedRoute = NavigationRoutes.HOME
        )

        val type =  HomeFilterType.MOVIE

        val chipGroup = ChipGroupComponent(
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

        val container = AppContainerComponent(
            bottomBar = bottomBar,
            chipGroup = chipGroup,
            components = listOf(AppMainContentComponent())
        )

        return Screen(
            components = listOf(container)
        )
    }
}
