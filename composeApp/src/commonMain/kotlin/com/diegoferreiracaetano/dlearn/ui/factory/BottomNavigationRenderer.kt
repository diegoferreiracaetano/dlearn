package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigation
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigationBar
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class BottomNavigationRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val bottomNav = component as? BottomNavigationComponent ?: return
        val nav = AppBottomNavigation(
            selectedRoute = bottomNav.selectedRoute.orEmpty(),
            onTabSelected = actions.onTabSelected
        )
        AppBottomNavigationBar(
            items = nav.items,
            selectedRoute = nav.selectedRoute,
            onTabSelected = nav.onTabSelected
        )
    }
}
