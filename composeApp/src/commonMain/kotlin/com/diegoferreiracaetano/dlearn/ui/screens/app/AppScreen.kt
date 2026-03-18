package com.diegoferreiracaetano.dlearn.ui.screens.app

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppAction
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppImageType
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import com.diegoferreiracaetano.dlearn.ui.util.decodeToScreen
import dlearn.composeapp.generated.resources.Res
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun AppScreen(
    path: String,
    params: Map<String, String>? = null,
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onNavigate: (AppAction.Navigation) -> Unit,
    onDeeplink: (AppAction.Deeplink) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(path, params) {
        viewModel.loadContent(path, params)
    }

    val actions = remember(onTabSelected, onItemClick, onNavigate, onDeeplink, viewModel) {
        ComponentActions(
            onItemClick = onItemClick,
            onTabSelected = onTabSelected,
            onAction = { action ->
                when (action) {
                    is AppAction.Navigation -> onNavigate(action)
                    is AppAction.Deeplink -> onDeeplink(action)
                    is AppAction.AppCall -> viewModel.handleAction(action)
                }
            },
            onRetry = viewModel::retry
        )
    }

    AppContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )
}

@Composable
fun AppScreen(
    actions: ComponentActions,
    modifier: Modifier = Modifier,
    path: String = actions.currentRoute,
    params: Map<String, String>? = null,
) {
    AppScreen(
        path = path,
        params = params,
        onTabSelected = actions.onTabSelected,
        onItemClick = actions.onItemClick,
        onNavigate = { action -> actions.onAction(action) },
        onDeeplink = { action -> actions.onAction(action) },
        modifier = modifier
    )
}

@Composable
fun AppContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
   uiState.Render(
       actions = actions,
       modifier = modifier
   )
}

@OptIn(ExperimentalResourceApi::class)
@Preview
@Composable
fun AppScreenWatchlistPreview() {
    val watchlistEmptyMock = Screen(
        components = listOf(
            AppEmptyStateComponent(
                title = "WATCHLIST_EMPTY_TITLE",
                description = "WATCHLIST_EMPTY_DESCRIPTION",
                image = AppImageType.WATCHLIST
            )
        )
    )

    val uiState = UIState.Success(watchlistEmptyMock)


    DLearnTheme(
        darkTheme = false,
    ) {
        AppContent(
            uiState = uiState,
            actions = ComponentActions(
                onItemClick = {},
                onAction = {},
                onRetry = {}
            ),
            modifier = Modifier.fillMaxSize()
        )
    }
}
