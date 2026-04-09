package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import com.diegoferreiracaetano.dlearn.designsystem.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalContentMaxHeight
import com.diegoferreiracaetano.dlearn.ui.util.TestTags
import com.diegoferreiracaetano.dlearn.ui.util.maybeHeight

class AppLoadingRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val maxHeight = LocalContentMaxHeight.current

        AppLoading(
            modifier = modifier
                .fillMaxSize()
                .maybeHeight(maxHeight)
                .testTag(TestTags.Components.LOADING_INDICATOR)
        )
    }
}
