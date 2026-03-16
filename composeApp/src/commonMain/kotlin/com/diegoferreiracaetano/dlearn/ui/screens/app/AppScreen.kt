package com.diegoferreiracaetano.dlearn.ui.screens.app

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.sdui.AppErrorComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppLoadingComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.koin.compose.koinInject

@Composable
fun AppScreen(
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: AppViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions = ComponentActions(
        onItemClick = onItemClick,
        onRetry = viewModel::retry
    )

    AppContent(
        uiState = uiState,
        actions = actions,
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
