package com.diegoferreiracaetano.dlearn.ui.screens.movie

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.diegoferreiracaetano.dlearn.designsystem.components.error.AppError
import com.diegoferreiracaetano.dlearn.designsystem.components.loading.AppLoading
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.factory.RenderComponentFactory
import com.diegoferreiracaetano.dlearn.ui.screens.movie.state.MovieDetailUiState
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppExpandableSectionComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.CarouselComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.UserRowComponent
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

    val actions = remember(onBackClick, onItemClick, viewModel) {
        ComponentActions(
            onItemClick = onItemClick,
            onBackClick = onBackClick,
            onRetry = viewModel::retry
        )
    }

    when (val state = uiState) {
        is MovieDetailUiState.Loading -> AppLoading(modifier = modifier)
        is MovieDetailUiState.Error -> AppError(
            throwable = state.throwable,
            onPrimary = viewModel::retry,
            modifier = modifier
        )

        is MovieDetailUiState.Success -> {
            MovieDetailListContent(
                components = state.screen.components,
                actions = actions,
                modifier = modifier,
            )
        }
    }
}

@Composable
fun MovieDetailListContent(
    components: List<Component>,
    actions: ComponentActions,
    modifier: Modifier = Modifier
) {
    components.forEach { component ->
        RenderComponentFactory.Render(
            component = component,
            actions = actions,
            modifier = modifier
        )
    }
}

@Preview
@Composable
fun MovieDetailContentPreview() {
    val components = listOf(
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

    DLearnTheme(darkTheme = true) {
        MovieDetailListContent(
            components = components,
            actions = ComponentActions()
        )
    }
}
