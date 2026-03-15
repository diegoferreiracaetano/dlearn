package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class MainScreenBuilder(
    private val i18n: I18nProvider
) {
    fun build(
        userId: String,
        appVersion: Int,
        lang: String,
        type: HomeFilterType
    ): Screen {
        val topBar = AppTopBarComponent(
            title = i18n.getString(AppStringType.HOME_TITLE, lang),
            subtitle = i18n.getString(AppStringType.HOME_SUBTITLE, lang),
            imageUrl = "https://avatars.githubusercontent.com/u/1023?v=4",
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
                        label = i18n.getString(AppStringType.NAV_NEW, lang),
                        route = NavigationRoutes.NEW,
                        icon = AppIconType.NEW
                    ),
                    BottomNavItem(
                        label = i18n.getString(AppStringType.NAV_FAVORITES, lang),
                        route = NavigationRoutes.FAVORITE,
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
            id = "main_shell",
            components = listOf(container),
            showSearch = false
        )
    }
}
