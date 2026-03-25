package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.error.AppError
import com.diegoferreiracaetano.dlearn.designsystem.util.rememberNetworkManager
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalContentMaxHeight
import com.diegoferreiracaetano.dlearn.ui.util.toUiData

class AppErrorRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {
        val errorComponent = component as? AppErrorComponent
        val maxHeight = LocalContentMaxHeight.current

        val appErrorData = errorComponent?.error?.toUiData()

        appErrorData?.let { errorData ->
            AppError(
                modifier = modifier.fillMaxSize().height(maxHeight),
                errorData = errorData,
                onPrimary = actions.onRetry
            )
        }
    }
}
