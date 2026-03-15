package com.diegoferreiracaetano.dlearn.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.search.state.SearchUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject

@Composable
fun SearchScreen(
    onTabSelected: (String) -> Unit,
    onItemClick: (String) -> Unit,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SearchViewModel = koinInject(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val query by viewModel.query.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        // viewModel.onQueryChange("") // Comentado ou chamado apenas para resetar se necessário
    }

    val actions = remember(onTabSelected, onItemClick, onBackClick, viewModel) {
        ComponentActions(
            onTabSelected = onTabSelected,
            onItemClick = onItemClick,
            onBackClick = onBackClick,
            onSearch = { /* onSearch comentado conforme solicitado */ },
            onQueryChange = viewModel::onQueryChange,
            onRetry = viewModel::retry,
            searchQuery = query
        )
    }

    SearchContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier
    )
}

@Composable
fun SearchContent(
    uiState: SearchUiState,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    when (val state = uiState) {
        is SearchUiState.Loading -> RenderComponentFactory.Render(
            component = AppLoadingComponent,
            actions = actions,
            modifier = modifier
        )
        is SearchUiState.Error -> RenderComponentFactory.Render(
            component = AppErrorComponent(state.throwable),
            actions = actions,
            modifier = modifier
        )
        is SearchUiState.Success -> {
            RenderComponentFactory.Render(
                components = state.screen.components,
                actions = actions,
                modifier = modifier
            )
        }
    }
}

@Preview
@Composable
fun SearchScreenLoadingPreview() {
    DLearnTheme {
        SearchContent(
            uiState = SearchUiState.Loading,
            actions = ComponentActions()
        )
    }
}

@Preview
@Composable
fun SearchScreenErrorPreview() {
    DLearnTheme {
        SearchContent(
            uiState = SearchUiState.Error(Throwable("Erro de conexão")),
            actions = ComponentActions()
        )
    }
}

@Preview
@Composable
fun SearchScreenEmptyPreview() {
    val screen = Screen(
        id = "search_empty",
        components = listOf(
            AppSearchBarComponent(
                query = "Unknown Movie",
                placeholder = "Buscar...",
                components = listOf(
                    AppEmptyStateComponent(
                        title = "Nenhum resultado encontrado",
                        description = "Não encontramos filmes para \"Unknown Movie\". Tente outros termos.",
                        image = AppImageType.SEARCH
                    ) as Component
                )
            ) as Component
        )
    )
    DLearnTheme {
        SearchContent(
            uiState = SearchUiState.Success(screen),
            actions = ComponentActions()
        )
    }
}

@Preview
@Composable
fun SearchScreenSuccessPreview() {
    val screen = Screen(
        id = "search_success",
        components = listOf(
            AppSearchBarComponent(
                query = "Spider-Man",
                placeholder = "Buscar...",
                components = listOf(
                    AppListComponent(
                        components = emptyList()
                    ) as Component
                )
            ) as Component
        )
    )
    DLearnTheme {
        SearchContent(
            uiState = SearchUiState.Success(screen),
            actions = ComponentActions()
        )
    }
}
