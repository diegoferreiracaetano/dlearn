package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.ui.screens.main.MainContent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppMainContentRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {
        MainContent(
            route = actions.currentRoute,
            onTabSelected = actions.onTabSelected,
            onItemClick = actions.onItemClick,
            onClose = actions.onClose,
            onShowSearchChanged = actions.onShowSearchChanged,
            modifier = modifier.padding(vertical = 8.dp)
        )
    }
}
