package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.sdui.*
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

        val container = AppContainerComponent(
            bottomBar = bottomBar,
            components = listOf(AppMainContentComponent())
        )

        return Screen(
            components = listOf(container)
        )
    }
}
