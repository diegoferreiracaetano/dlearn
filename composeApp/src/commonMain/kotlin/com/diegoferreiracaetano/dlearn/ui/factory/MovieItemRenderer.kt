package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.AppMovieItem
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.toMovieItem

class MovieItemRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val movieItem = component as? MovieItemComponent ?: return

        AppMovieItem(
            movie = movieItem.toMovieItem(),
            onClick = { actions.onItemClick(movieItem.id) },
            modifier = modifier.padding(horizontal = 16.dp)
        )
    }
}
