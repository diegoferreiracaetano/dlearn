package com.diegoferreiracaetano.dlearn.ui.screens.movie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.Render
import com.diegoferreiracaetano.dlearn.ui.viewmodel.movie.MovieDetailViewModel
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailScreen(
    movieId: String,
    onBackClick: () -> Unit,
    onMovieClick: (String) -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = koinInject { parametersOf(movieId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val actions =
        remember(onBackClick, onMovieClick, onItemClick, viewModel) {
            ComponentActions(
                onMovieClick = onMovieClick,
                onItemClick = onItemClick,
                onBackClick = onBackClick,
                onRetry = viewModel::retry,
                onAction = viewModel::execute,
            )
        }

    MovieDetailContent(
        uiState = uiState,
        actions = actions,
        modifier = modifier,
    )
}

@Composable
fun MovieDetailContent(
    uiState: UIState<Screen>,
    actions: ComponentActions,
    modifier: Modifier = Modifier,
) {
    uiState.Render(actions, modifier)
}
