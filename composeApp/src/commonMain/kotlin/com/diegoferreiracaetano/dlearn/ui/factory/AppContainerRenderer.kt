@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppList
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppContainerRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val container = component as? AppContainerComponent ?: return
        AppContainer(
            modifier = modifier,
            topBar = {
                container.topBar?.let { topBar ->
                    RenderComponentFactory.Render(component = topBar, actions = actions)
                }
            },
            bottomBar = {
                container.bottomBar?.let { bottomBar ->
                    RenderComponentFactory.Render(component = bottomBar, actions = actions)
                }
            }
        ) { contentModifier ->
            AppList(
                modifier = contentModifier
            ) {
                items(container.components) { child ->
                    RenderComponentFactory.Render(component = child, actions = actions)
                }
            }
        }
    }
}
