package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.AppMovieDetailHeader
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.MovieItem
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions

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
                id = "",
                title = header.title,
                imageSource = AppImageSource.Url(header.imageUrl),
                year = header.year.orEmpty(),
                duration = header.duration.orEmpty(),
                genre = header.genre.orEmpty(),
                rating = header.rating
            ),
            modifier = modifier
        )
    }
}
