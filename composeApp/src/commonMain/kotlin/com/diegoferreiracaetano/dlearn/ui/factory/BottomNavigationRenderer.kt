package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigation
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppBottomNavigationBar
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppNavigationTab
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.ui.sdui.BottomNavigationComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toIcon

class BottomNavigationRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val bottomNav = component as? BottomNavigationComponent ?: return

        val mappedItems =
            bottomNav.items.map { item ->
                val icon = item.icon.toIcon() ?: Icons.Default.QuestionMark
                AppNavigationTab(
                    route = item.actionUrl,
                    label = item.label,
                    selectedIcon = icon,
                    unselectedIcon = icon,
                )
            }

        val selectedRoute = actions.currentRoute.takeIf { it.isNotEmpty() }
            ?: bottomNav.selectedActionUrl
            ?: AppNavigationRoute.HOME

        val nav =
            AppBottomNavigation(
                selectedRoute = selectedRoute,
                items = mappedItems,
                onTabSelected = actions.onTabSelected,
            )

        AppBottomNavigationBar(
            items = nav.items,
            selectedRoute = nav.selectedRoute,
            onTabSelected = nav.onTabSelected,
        )
    }
}
