package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.diegoferreiracaetano.dlearn.designsystem.components.carousel.AppMovieCarousel
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieCarouselComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toMovieItem

class MovieCarouselRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val carousel = component as? MovieCarouselComponent ?: return
        AppMovieCarousel(
            title = carousel.title,
            items = carousel.items.map { it.toMovieItem() },
            onMovieClick = { movie -> actions.onItemClick(movie.id) }
        )
    }
}
