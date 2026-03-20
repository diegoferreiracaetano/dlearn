package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppScreen
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppMainContentRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {

        when (actions.currentRoute) {
            NavigationRoutes.HOME -> AppScreen(
                actions = actions,
                modifier = modifier
            )

            NavigationRoutes.PROFILE -> AppScreen(
                actions = actions,
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
