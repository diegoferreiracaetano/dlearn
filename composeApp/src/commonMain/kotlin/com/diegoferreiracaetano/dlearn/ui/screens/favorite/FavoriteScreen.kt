package com.diegoferreiracaetano.dlearn.ui.screens.favorite

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
import com.diegoferreiracaetano.dlearn.ui.screens.favorite.state.FavoriteUiState
import com.diegoferreiracaetano.dlearn.ui.screens.main.LocalMainContainerState
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.koin.compose.koinInject

@Composable
fun FavoriteScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onClose: () -> Unit,
    onShowSearchChanged: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = koinInject(),
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
            is FavoriteUiState.Success -> {
                onShowSearchChanged(state.screen.showSearch)
                containerState.onMainLoading(false)
            }
            is FavoriteUiState.Loading -> containerState.onMainLoading(true)
            is FavoriteUiState.Error -> containerState.onMainError(state.throwable)
        }
    }

    (uiState as? FavoriteUiState.Success)?.let { state ->
        FavoriteListContent(
            components = state.screen.components,
            actions = actions,
            modifier = modifier,
        )
    }
}

@Composable
fun FavoriteListContent(
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
