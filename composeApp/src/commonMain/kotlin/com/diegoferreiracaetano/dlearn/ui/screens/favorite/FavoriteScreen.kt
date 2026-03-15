package com.diegoferreiracaetano.dlearn.ui.screens.favorite

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.koin.compose.koinInject

@Composable
fun FavoriteScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = remember(onItemClick, onTabSelected, viewModel) {
        ComponentActions(
            onItemClick = onItemClick,
            onTabSelected = onTabSelected,
            onRetry = viewModel::retry
        )
    }

    FavoriteContent(uiState, actions, modifier)

}

@Composable
fun FavoriteContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    uiState.Render(
        actions = actions,
        modifier = modifier
    )
}
