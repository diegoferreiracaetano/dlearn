package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.state.AppEmptyState
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toImageSource

class AppEmptyStateRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val emptyStateComponent = component as? AppEmptyStateComponent ?: return

        AppEmptyState(
            title = emptyStateComponent.title,
            description = emptyStateComponent.description,
            imageSource = emptyStateComponent.image.toImageSource(),
            onPrimary = actions.onRetry,
            modifier = modifier
        )
    }
}
