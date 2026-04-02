package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

interface ComponentRenderer {
    @Composable
    fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    )
}
