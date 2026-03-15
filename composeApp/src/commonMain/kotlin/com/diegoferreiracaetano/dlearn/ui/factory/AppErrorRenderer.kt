package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.error.AppError
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalContentMaxHeight

class AppErrorRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {
        val errorComponent = component as? AppErrorComponent
        val maxHeight = LocalContentMaxHeight.current

        Box(
            modifier = modifier.height(maxHeight),
            contentAlignment = Alignment.Center
        ) {
            AppError(
                modifier = Modifier,
                throwable = errorComponent?.throwable,
                onPrimary = actions.onRetry
            )
        }
    }
}
