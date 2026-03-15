package com.diegoferreiracaetano.dlearn.ui.screens.search

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
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

    // Ao entrar na tela, não fazemos chamada automática com query, 
    // apenas garantimos que o estado inicial seja carregado (ex: campo vazio)
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
            onRetry = viewModel::retry
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
        SearchContent(
            uiState = UIState.Loading,
            actions = ComponentActions()
        )
    }
}

@Preview
@Composable
fun SearchScreenErrorPreview() {
    DLearnTheme {
        SearchContent(
            uiState = UIState.Error(Throwable("Erro de conexão")),
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
            uiState = UIState.Success(screen),
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
            uiState = UIState.Success(screen),
            actions = ComponentActions()
        )
    }
}
