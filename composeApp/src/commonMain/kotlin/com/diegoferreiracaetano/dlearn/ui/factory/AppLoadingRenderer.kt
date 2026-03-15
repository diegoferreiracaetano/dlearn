package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalContentMaxHeight

class AppLoadingRenderer : ComponentRenderer {
    @Composable
    override fun Render(component: Component, actions: ComponentActions, modifier: Modifier) {
        val maxHeight = LocalContentMaxHeight.current

        AppLoading(
            modifier =  modifier.height(maxHeight)
        )
    }
}
