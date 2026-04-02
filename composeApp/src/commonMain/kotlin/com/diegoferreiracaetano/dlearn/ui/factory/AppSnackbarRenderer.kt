package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.SnackbarType
import com.diegoferreiracaetano.dlearn.designsystem.components.alert.showAppSnackBar
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSnackbarType
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState

class AppSnackbarRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val snackbarComponent = component as? AppSnackbarComponent ?: return
        val snackbarHostState = LocalSnackbarHostState.current

        val snackbarType =
            when (snackbarComponent.snackbarType) {
                AppSnackbarType.SUCCESS -> SnackbarType.SUCCESS
                AppSnackbarType.ERROR -> SnackbarType.ERROR
                AppSnackbarType.WARNING -> SnackbarType.WARNING
            }

        LaunchedEffect(snackbarComponent) {
            snackbarHostState.showAppSnackBar(
                scope = this,
                message = snackbarComponent.message,
                type = snackbarType,
            )
        }
    }
}
