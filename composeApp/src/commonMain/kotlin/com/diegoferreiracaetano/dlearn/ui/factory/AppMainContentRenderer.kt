package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.ui.screens.app.AppScreen
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppMainContentRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {

        when (actions.currentRoute) {
            AppNavigationRoute.HOME,
            AppNavigationRoute.PROFILE,
            AppNavigationRoute.WATCHLIST,
            AppNavigationRoute.FAVORITE,
            AppNavigationRoute.USERS -> AppScreen(
                actions = actions,
                modifier = modifier
            )
        }
    }
}
