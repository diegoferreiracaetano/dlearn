package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
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
        lang: String,
        user: User? = null
    ): Screen {

        val selectedActionUrl = AppNavigationRoute.HOME

        val topBarTitle = user?.name ?: i18n.getString(AppStringType.HOME_TITLE, lang)
        val topBarSubtitle = if (user != null) i18n.getString(AppStringType.HOME_SUBTITLE, lang) else null
        val topBarImageUrl = user?.imageUrl ?: AppConstants.AVATAR_PLACEHOLDER

        val topBar = AppTopBarListComponent(
            selectedActionUrl = selectedActionUrl,
            items = listOf(
                AppTopBarItem(
                    actionUrl = AppNavigationRoute.HOME,
                    topBar = AppTopBarComponent(
                        title = topBarTitle,
                        subtitle = topBarSubtitle,
                        imageUrl = topBarImageUrl,
                        showSearch = true
                    )
                ),
                AppTopBarItem(
                    actionUrl = AppNavigationRoute.WATCHLIST,
                    topBar =  AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_WATCHLIST, lang)
                    )
                ),
                AppTopBarItem(
                    actionUrl = AppNavigationRoute.FAVORITE,
                    topBar =  AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_FAVORITES, lang)
                    )
                ),
                AppTopBarItem(
                    actionUrl = AppNavigationRoute.PROFILE,
                    topBar =  AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_PROFILE, lang)
                    )
                )
            )
        )

        val bottomBar = BottomNavigationComponent(
            selectedActionUrl = selectedActionUrl,
            items = listOf(
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_HOME, lang),
                    actionUrl = AppNavigationRoute.HOME,
                    icon = AppIconType.HOME
                ),
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_WATCHLIST, lang),
                    actionUrl = AppNavigationRoute.WATCHLIST,
                    icon = AppIconType.WATCHLIST
                ),
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_FAVORITES, lang),
                    actionUrl = AppNavigationRoute.FAVORITE,
                    icon = AppIconType.FAVORITE
                ),
                BottomNavItem(
                    label = i18n.getString(AppStringType.NAV_PROFILE, lang),
                    actionUrl = AppNavigationRoute.PROFILE,
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
