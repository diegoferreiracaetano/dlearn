package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppList
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppListRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {
        val listComponent = component as? AppListComponent ?: return
        
        AppList(modifier = modifier.fillMaxSize()) {
            items(listComponent.components) { child ->
                RenderComponentFactory.Render(
                    component = child,
                    actions = actions
                )
            }
        }
    }
}
