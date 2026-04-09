package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.diegoferreiracaetano.dlearn.designsystem.components.state.AppEmptyState
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalContentMaxHeight
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.util.maybeHeight
import com.diegoferreiracaetano.dlearn.ui.util.toImageSource

class AppEmptyStateRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val emptyStateComponent = component as? AppEmptyStateComponent ?: return
        val maxHeight = LocalContentMaxHeight.current

        AppEmptyState(
            title = emptyStateComponent.title,
            description = emptyStateComponent.description,
            imageSource = emptyStateComponent.image.toImageSource(),
            onPrimary = actions.onRetry,
            modifier = modifier
                .fillMaxSize()
                .maybeHeight(maxHeight)
                .testTag(TestTags.Components.EMPTY_STATE),
        )
    }
}
