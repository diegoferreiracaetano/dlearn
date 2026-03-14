package com.diegoferreiracaetano.dlearn.ui.screens.new

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.list.AppList
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.main.LocalMainContainerState
import com.diegoferreiracaetano.dlearn.ui.screens.new.state.NewUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.koin.compose.koinInject

@Composable
fun NewScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    onShowSearchChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val containerState = LocalMainContainerState.current

    val actions = remember(onItemClick, onTabSelected, viewModel) {
        ComponentActions(
            onItemClick = onItemClick,
            onTabSelected = onTabSelected,
            onRetry = viewModel::retry
        )
    }

    LaunchedEffect(uiState) {
        when (val state = uiState) {
            is NewUiState.Success -> {
                onShowSearchChanged(state.screen.showSearch)
                containerState.onMainLoading(false)
            }
            is NewUiState.Loading -> containerState.onMainLoading(true)
            is NewUiState.Error -> containerState.onMainError(state.throwable)
        }
    }

    (uiState as? NewUiState.Success)?.let { state ->
        NewListContent(
            components = state.screen.components,
            actions = actions,
            modifier = modifier,
        )
    }
}

@Composable
fun NewListContent(
    components: List<Component>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    AppList(
        modifier = modifier.fillMaxSize()
    ) {
        items(components) { component ->
            RenderComponentFactory.Render(
                component = component,
                actions = actions
            )
        }
    }
}
