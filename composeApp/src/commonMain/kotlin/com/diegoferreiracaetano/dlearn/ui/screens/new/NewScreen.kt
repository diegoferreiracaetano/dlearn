package com.diegoferreiracaetano.dlearn.ui.screens.new

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun NewScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: NewViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = remember(onItemClick, onTabSelected, viewModel) {
        ComponentActions(
            onItemClick = onItemClick,
            onTabSelected = onTabSelected,
            onRetry = viewModel::retry
        )
    }

    NewContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )
}

@Composable
fun NewContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    uiState.Render(
        actions = actions,
        modifier = modifier
    )
}

@Preview
@Composable
fun NewScreenPreview() {
    DLearnTheme {
        NewContent(
            uiState = UIState.Success(Screen(id = "new", components = emptyList())),
            actions = ComponentActions()
        )
    }
}
