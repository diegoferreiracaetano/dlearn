package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.AppMovieDetailHeader
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.MovieItem
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.WatchProvider
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
        val uriHandler = LocalUriHandler.current

        val movieItem = MovieItem(
            id = "",
            title = header.title,
            imageSource = AppImageSource.Url(header.imageUrl),
            year = header.year ?: "",
            duration = header.duration ?: "",
            genre = header.genre ?: "",
            rating = header.rating ?: 0.0,
            youtubeVideoId = header.trailerId
        )

        val providers = header.providers.map {
            WatchProvider(
                name = it.name,
                icon = AppImageSource.Url(it.iconUrl),
                priceInfo = it.priceInfo,
                watchUrl = it.actionUrl
            )
        }

        AppMovieDetailHeader(
            movie = movieItem,
            providers = providers,
            onProviderClick = { provider ->
                provider.watchUrl?.let { uriHandler.openUri(it) }
            },
            isFavorite = header.isFavorite,
            isInList = header.isInList,
            onFavoriteClick = { /* TODO: Implement action in ComponentActions */ },
            onAddToListClick = { /* TODO: Implement action in ComponentActions */ },
            modifier = modifier
        )
    }
}
