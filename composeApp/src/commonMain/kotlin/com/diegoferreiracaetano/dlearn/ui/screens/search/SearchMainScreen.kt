package com.diegoferreiracaetano.dlearn.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSearchBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SearchMainScreen(
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchMainViewModel = koinInject()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var query by remember { mutableStateOf("") }

    val actions = remember(onItemClick, onBackClick, query) {
        ComponentActions(
            onItemClick = onItemClick,
            onBackClick = onBackClick,
            onQueryChange = { query = it },
            onRetry = viewModel::retry,
            searchQuery = query
        )
    }

    SearchScreenContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )
}

@Composable
private fun SearchScreenContent(
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
fun SearchScreenLoadingPreview() {
    DLearnTheme {
        SearchScreenContent(
            uiState = UIState.Loading,
            actions = ComponentActions()
        )
    }
}

@Preview
@Composable
fun SearchScreenErrorPreview() {
    DLearnTheme {
        SearchScreenContent(
            uiState = UIState.Error(Throwable("Erro de conexão")),
            actions = ComponentActions()
        )
    }
}

@Preview
@Composable
fun SearchScreenSuccessPreview() {
    val screen = Screen(
        id = "search_shell",
        components = listOf(
            AppSearchBarComponent(
                query = "Spider-Man",
                placeholder = "Buscar..."
            )
        )
    )
    DLearnTheme {
        SearchScreenContent(
            uiState = UIState.Success(screen),
            actions = ComponentActions(searchQuery = "Spider-Man")
        )
    }
}
