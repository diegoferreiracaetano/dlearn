package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppScreen
import com.diegoferreiracaetano.dlearn.ui.screens.home.HomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.profile.ProfileScreen
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppMainContentRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {

        when (actions.currentRoute) {
            NavigationRoutes.HOME -> HomeScreen(
                onTabSelected = actions.onTabSelected,
                onItemClick = actions.onItemClick,
                onSearchClick = actions.onSearchClick,
                modifier = modifier
            )

            NavigationRoutes.PROFILE -> ProfileScreen(
                onTabSelected = actions.onTabSelected,
                onItemClick = actions.onItemClick,
                modifier = modifier
            )

            NavigationRoutes.WATCHLIST -> AppScreen(
                actions = actions,
                modifier = modifier
            )

            NavigationRoutes.FAVORITE -> AppScreen(
                actions = actions,
                modifier = modifier
            )
        }
    }
}
