package com.diegoferreiracaetano.dlearn.ui.screens.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppImageType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import com.diegoferreiracaetano.dlearn.ui.viewmodel.app.AppViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun AppScreen(
    path: String,
    params: Map<String, String>? = null,
    onMovieClick: (String) -> Unit = {},
    onBackClick: () -> Unit = {},
    onClose: () -> Unit = {},
    onTabSelected: (String) -> Unit = {},
    onItemClick: (String) -> Unit = {},
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(path, params) {
        viewModel.fetch(path, params)
    }

    val actions =
        remember(
            onMovieClick,
            onItemClick,
            onTabSelected,
            onBackClick,
            onClose,
        ) {
            ComponentActions(
                onMovieClick = onMovieClick,
                onItemClick = onItemClick,
                onTabSelected = onTabSelected,
                onBackClick = onBackClick,
                onRetry = viewModel::retry,
                onAction = viewModel::action,
                onClose = onClose,
            )
        }

    AppContent(uiState, actions, modifier)
}

@Composable
fun AppContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier,
) {
    uiState.Render(
        actions = actions,
        modifier = modifier,
    )
}

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun AppScreenSuccessPreview() {
    val watchlistEmptyMock =
        Screen(
            components =
            listOf(
                AppEmptyStateComponent(
                    title = "WATCHLIST_EMPTY_TITLE",
                    description = "WATCHLIST_EMPTY_DESCRIPTION",
                    image = AppImageType.WATCHLIST,
                ),
            ),
        )

    DLearnTheme {
        AppContent(
            uiState = UIState.Success(watchlistEmptyMock),
            actions = ComponentActions(onRetry = {}),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
fun AppScreenLoadingPreview() {
    DLearnTheme {
        AppContent(
            uiState = UIState.Loading,
            actions = ComponentActions(onRetry = {}),
            modifier = Modifier.fillMaxSize(),
        )
    }
}

@Preview
@Composable
fun AppScreenErrorPreview() {
    DLearnTheme {
        AppContent(
            uiState = UIState.Error(Exception("Error message")),
            actions = ComponentActions(onRetry = {}),
            modifier = Modifier.fillMaxSize(),
        )
    }
}
