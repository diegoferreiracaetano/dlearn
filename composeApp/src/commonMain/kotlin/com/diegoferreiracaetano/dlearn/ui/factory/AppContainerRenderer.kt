@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalContentMaxHeight
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import com.diegoferreiracaetano.dlearn.ui.util.LocalTopBarManager

class AppContainerRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val container = component as? AppContainerComponent ?: return
        val snackbarHostState = remember { SnackbarHostState() }
        val topBarManager = LocalTopBarManager.current

        CompositionLocalProvider(
            LocalSnackbarHostState provides snackbarHostState
        ) {
            AppContainer(
                modifier = modifier.fillMaxSize(),
                snackBarHostState = snackbarHostState,
                topBar = {
                    val topBarState = topBarManager.state
                    val topBarComponent = topBarState.component ?: container.topBar
                    val topBarActions = if (topBarState.component != null) topBarState.actions else actions

                    topBarComponent?.let {
                        RenderComponentFactory.Render(component = it, actions = topBarActions)
                    }
                },
                searchBar = {
                    container.searchBar?.let { searchBar ->
                        RenderComponentFactory.Render(component = searchBar, actions = actions)
                    }
                },
                chipGroup = {
                    container.chipGroup?.let { chipGroup ->
                        RenderComponentFactory.Render(component = chipGroup, actions = actions)
                    }
                },
                bottomBar = {
                    container.bottomBar?.let { bottomBar ->
                        RenderComponentFactory.Render(component = bottomBar, actions = actions)
                    }
                }
            ) { baseModifier ->

                BoxWithConstraints(
                    modifier = baseModifier.fillMaxSize(),
                ) {

                    CompositionLocalProvider(
                        LocalContentMaxHeight provides maxHeight
                    ) {
                        RenderComponentFactory.Render(
                            component = AppListComponent(components = container.components),
                            actions = actions,
                            modifier = modifier
                        )
                    }
                }
            }
        }
    }
}
