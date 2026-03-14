@file:OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)

package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppList
import com.diegoferreiracaetano.dlearn.designsystem.components.navigation.AppContainer
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState

class AppContainerRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val container = component as? AppContainerComponent ?: return
        val snackbarHostState = remember { SnackbarHostState() }

        CompositionLocalProvider(LocalSnackbarHostState provides snackbarHostState) {
            AppContainer(
                modifier = modifier,
                snackBarHostState = snackbarHostState,
                isLoading = actions.isLoading,
                error = actions.error,
                onRetry = actions.onRetry,
                topBar = {
                    container.topBar?.let { topBar ->
                        RenderComponentFactory.Render(component = topBar, actions = actions)
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
            ) { contentModifier ->
                BoxWithConstraints(modifier = contentModifier) {
                    AppList(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(container.components) { child ->
                            RenderComponentFactory.Render(component = child, actions = actions)
                        }
                    }
                }
            }
        }
    }
}
