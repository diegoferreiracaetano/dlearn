package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.AppMovieDetailHeader
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.MovieItem
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.util.getLogger

class AppMovieDetailHeaderRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val header = component as? AppMovieDetailHeaderComponent ?: return
        
        AppMovieDetailHeader(
            movie = MovieItem(
                id = header.trailerId.orEmpty(),
                title = header.title,
                imageSource = AppImageSource.Url(header.imageUrl),
                year = header.year.orEmpty(),
                duration = header.duration.orEmpty(),
                genre = header.genre.orEmpty(),
                rating = header.rating,
                youtubeVideoId = header.trailerId
            ),
            onPlayClick = {
                getLogger().d("LOGGER", "Usuário iniciou o trailer inline")
            },
            onDownloadClick = { header.downloadActionUrl?.let { actions.onItemClick(it) } },
            onShareClick = { header.shareActionUrl?.let { actions.onItemClick(it) } },
            modifier = modifier.padding(bottom = 24.dp)
        )
    }
}
