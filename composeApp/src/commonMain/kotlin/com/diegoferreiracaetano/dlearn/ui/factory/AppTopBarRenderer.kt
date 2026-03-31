@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppTopBarRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val topBar = component as? AppTopBarComponent ?: return

        AppTopBar(
            modifier = modifier,
            title = topBar.title,
            subtitle = topBar.subtitle,
            profileImageSource = topBar.imageUrl?.let { AppImageSource.Url(it) },
            onBack = actions.onBackClick,
            onSearchClick = if (topBar.showSearch) actions.onSearchClick else null
        )
    }
}
