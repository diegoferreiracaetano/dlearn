package com.diegoferreiracaetano.dlearn.ui.screens.movie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.error.AppError
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.home.LoadingScreen
import com.diegoferreiracaetano.dlearn.ui.screens.movie.state.MovieDetailUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.koinInject
import org.koin.core.parameter.parametersOf

@Composable
fun MovieDetailScreen(
    movieId: String,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = koinInject { parametersOf(movieId) },
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    MovieDetailScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onItemClick = onItemClick,
        onRetry = viewModel::retry,
        modifier = modifier
    )
}

@Composable
internal fun MovieDetailScreen(
    uiState: MovieDetailUiState,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    when (uiState) {
        is MovieDetailUiState.Success -> {
            MovieDetailContent(
                screen = uiState.screen,
                onBackClick = onBackClick,
                onItemClick = onItemClick,
                modifier = modifier,
            )
        }

        is MovieDetailUiState.Loading -> LoadingScreen()
        is MovieDetailUiState.Error -> {
            AppError(
                throwable = uiState.throwable,
                modifier = modifier,
                onPrimary = onRetry,
                onClose = onBackClick
            )
        }
    }
}

@Composable
private fun MovieDetailContent(
    screen: Screen,
    onBackClick: () -> Unit,
    onItemClick: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val actions = remember(onBackClick, onItemClick) {
        ComponentActions(
            onItemClick = onItemClick,
            onBackClick = onBackClick
        )
    }

    screen.components.forEach { component ->
        RenderComponentFactory.Render(
            modifier = modifier,
            component = component,
            actions = actions
        )
    }
}

@Preview
@Composable
fun MovieDetailContentPreview() {
    val screen = Screen(
        id = "movie_detail",
        components = listOf(
            AppContainerComponent(
                topBar = AppTopBarComponent(
                    title = "Interstellar",
                    showSearch = false
                ),
                components = listOf(
                    AppMovieDetailHeaderComponent(
                        title = "Interstellar",
                        imageUrl = "https://image.tmdb.org/t/p/w500/gEU2QniE6E77NI6lCU6MxlNBvIx.jpg",
                        year = "2014",
                        duration = "169 min",
                        genre = "Sci-Fi",
                        rating = 8.6,
                        trailerId = "zSWdZVtXT7E",
                        playText = "Trailer",
                        downloadActionUrl = "dlearn://download/157336",
                        shareActionUrl = "dlearn://share/157336"
                    ),
                    AppExpandableSectionComponent(
                        title = "Storyline",
                        text = "A team of explorers travel through a wormhole in space in an attempt to ensure humanity's survival. The crew of the Endurance must travel to three potential planets to find a new home for mankind."
                    ),
                    CarouselComponent(
                        title = "Cast & Crew",
                        items = listOf(
                            UserRowComponent(
                                name = "Matthew McConaughey",
                                role = "Cooper",
                                imageUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png"
                            ),
                            UserRowComponent(
                                name = "Anne Hathaway",
                                role = "Brand",
                                imageUrl = "https://cdn-icons-png.flaticon.com/512/149/149071.png"
                            )
                        )
                    )
                )
            )
        )
    )

    DLearnTheme(darkTheme = true) {
        MovieDetailContent(
            screen = screen,
            onBackClick = {},
            onItemClick = {}
        )
    }
}

@Preview
@Composable
private fun MovieDetailScreenLoadingPreview() {
    DLearnTheme {
        MovieDetailScreen(
            uiState = MovieDetailUiState.Loading,
            onBackClick = {},
            onItemClick = {},
            onRetry = {}
        )
    }
}

@Preview
@Composable
private fun MovieDetailScreenErrorPreview() {
    DLearnTheme {
        MovieDetailScreen(
            uiState = MovieDetailUiState.Error(Throwable("Failed to load movie details")),
            onBackClick = {},
            onItemClick = {},
            onRetry = {}
        )
    }
}
