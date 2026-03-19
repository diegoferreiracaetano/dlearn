package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppIconType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMainContentComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarItem
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavItem
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
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

        val selectedRoute = NavigationRoutes.HOME

        val topBar = AppTopBarListComponent(
            selectedRoute = selectedRoute,
            items = listOf(
                AppTopBarItem(
                    route = NavigationRoutes.HOME,
                    topBar = AppTopBarComponent(
                        title = i18n.getString(AppStringType.HOME_TITLE, lang),
                        subtitle = i18n.getString(AppStringType.HOME_SUBTITLE, lang),
                        imageUrl = AppConstants.AVATAR_PLACEHOLDER,
                        showSearch = true
                    )
                ),
                AppTopBarItem(
                    route = NavigationRoutes.WATCHLIST,
                    topBar =  AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_WATCHLIST, lang)
                    )
                ),
                AppTopBarItem(
                    route = NavigationRoutes.FAVORITE,
                    topBar =  AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_FAVORITES, lang)
                    )
                )
,
                AppTopBarItem(
                    route = NavigationRoutes.PROFILE,
                    topBar =  AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_PROFILE, lang)
                    )
                )
            )
        )

        val bottomBar = BottomNavigationComponent(
            selectedRoute = selectedRoute,
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
            )
        )

        val container = AppContainerComponent(
            topBar = topBar,
            bottomBar = bottomBar,
            components = listOf(AppMainContentComponent())
        )

        return Screen(
            components = listOf(container)
        )
    }
}
