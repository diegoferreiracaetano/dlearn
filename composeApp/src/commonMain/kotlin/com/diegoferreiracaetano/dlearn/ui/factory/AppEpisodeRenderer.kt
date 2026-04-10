package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.AppMovieItem
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.MovieItem
import com.diegoferreiracaetano.dlearn.designsystem.theme.DLearnTheme
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEpisodeComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import org.jetbrains.compose.ui.tooling.preview.Preview

class AppEpisodeRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier,
    ) {
        val episode = component as? AppEpisodeComponent ?: return

        val movieItem = MovieItem(
            id = episode.id,
            title = episode.title,
            imageSource = AppImageSource.Url(episode.imageUrl),
            duration = episode.duration,
            isPremium = episode.isPremium,
            primaryInfo = if (episode.isPremium) "Premium" else null,
            secondaryInfo = episode.duration,
            genre = episode.description,
            type = "Episode"
        )

        AppMovieItem(
            movie = movieItem,
            onClick = { episode.actionUrl?.let { actions.onAction(it) } },
            modifier = modifier.padding(vertical = 8.dp, horizontal = 16.dp)
        )
    }
}

@Preview
@Composable
fun AppEpisodeRendererPreview() {
    DLearnTheme {
        AppEpisodeRenderer().Render(
            component = AppEpisodeComponent(
                id = "1",
                title = "Episode 1",
                description = "This is a description of the first episode.",
                imageUrl = "",
                duration = "45m",
                isPremium = true
            ),
            actions = ComponentActions(),
            modifier = Modifier
        )
    }
}
