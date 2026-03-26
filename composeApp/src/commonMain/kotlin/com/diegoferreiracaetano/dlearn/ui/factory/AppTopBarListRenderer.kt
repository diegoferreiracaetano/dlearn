@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppTopBar
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.TopBarConfig
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

class AppTopBarListRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val topBarList = component as? AppTopBarListComponent ?: return

        if (topBarList.items.isEmpty())
            return
        else {
            val configs = topBarList.items.map { item ->
                TopBarConfig(
                    route = item.actionUrl,
                    title = item.topBar.title,
                    subtitle = item.topBar.subtitle,
                    profileImageSource = item.topBar.imageUrl?.let {
                        AppImageSource.Url(it)
                    },

                    onSearchClick = actions.onSearchClick
                )
            }

            AppTopBar(configs, topBarList.selectedActionUrl)
        }
    }
}
