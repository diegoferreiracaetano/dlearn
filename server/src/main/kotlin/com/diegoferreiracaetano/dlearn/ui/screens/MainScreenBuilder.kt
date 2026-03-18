package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.ComponentIds
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
        val topBar = AppTopBarComponent(
            title = i18n.getString(AppStringType.HOME_TITLE, lang),
            subtitle = i18n.getString(AppStringType.HOME_SUBTITLE, lang),
            imageUrl = AppConstants.AVATAR_PLACEHOLDER,
            showSearch = false
        )

        val container = AppContainerComponent(
            topBar = topBar,
            bottomBar = BottomNavigationComponent(
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
            ),
            components = listOf(AppMainContentComponent())
        )

        return Screen(
            id = ComponentIds.MAIN_SHELL,
            components = listOf(container),
            showSearch = false
        )
    }
}
