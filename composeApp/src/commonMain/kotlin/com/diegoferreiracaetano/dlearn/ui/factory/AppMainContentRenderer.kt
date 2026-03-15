package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.screens.favorite.FavoriteScreen
import com.diegoferreiracaetano.dlearn.ui.screens.home.HomeScreen
import com.diegoferreiracaetano.dlearn.ui.screens.new.NewScreen
import com.diegoferreiracaetano.dlearn.ui.screens.profile.ProfileScreen
import com.diegoferreiracaetano.dlearn.ui.screens.search.SearchScreen
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

            NavigationRoutes.NEW -> NewScreen(
                onTabSelected = actions.onTabSelected,
                onItemClick = actions.onItemClick,
                modifier = modifier
            )

            NavigationRoutes.FAVORITE -> FavoriteScreen(
                onTabSelected = actions.onTabSelected,
                onItemClick = actions.onItemClick,
                modifier = modifier
            )
        }
    }
}
