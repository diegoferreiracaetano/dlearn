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
        route: String,
        type: HomeFilterType
    ): Screen {
        val topBar = when (route) {
            NavigationRoutes.HOME -> AppTopBarComponent(
                title = i18n.getString(AppStringType.HOME_TITLE, lang),
                subtitle = i18n.getString(AppStringType.HOME_SUBTITLE, lang),
                imageUrl = "https://avatars.githubusercontent.com/u/1023?v=4",
                showSearch = false
            )
            NavigationRoutes.PROFILE -> AppTopBarComponent(
                title = i18n.getString(AppStringType.PROFILE_TITLE, lang)
            )
            NavigationRoutes.FAVORITE -> AppTopBarComponent(
                title = i18n.getString(AppStringType.NAV_FAVORITES, lang)
            )
            NavigationRoutes.NEW -> AppTopBarComponent(
                title = i18n.getString(AppStringType.NAV_NEW, lang)
            )
            else -> AppTopBarComponent(
                title = i18n.getString(AppStringType.APP_NAME, lang)
            )
        }

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
                selectedRoute = route
            ),
            components = listOf(AppMainContentComponent())
        )

        return Screen(
            id = "main_shell_$route",
            components = listOf(container),
            showSearch = false
        )
    }
}
