package com.diegoferreiracaetano.dlearn.ui.factory

import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import com.diegoferreiracaetano.dlearn.TmdbConstants.TAG_SCREEN_ERROR
import com.diegoferreiracaetano.dlearn.designsystem.components.image.AppImageSource
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.AppMovieDetailHeader
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.MovieItem
import com.diegoferreiracaetano.dlearn.designsystem.components.movie.WatchProvider
import com.diegoferreiracaetano.dlearn.ui.sdui.AppMovieDetailHeaderComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.util.ComponentActions
import com.diegoferreiracaetano.dlearn.ui.util.LocalSnackbarHostState
import com.diegoferreiracaetano.dlearn.ui.util.UriLauncher
import com.diegoferreiracaetano.dlearn.ui.util.UriResolutionException
import com.diegoferreiracaetano.dlearn.util.getLogger
import dlearn.composeapp.generated.resources.Res
import dlearn.composeapp.generated.resources.error_provider_link
import io.ktor.util.logging.error
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.getString

class AppMovieDetailHeaderRenderer : ComponentRenderer {
    @Composable
    override fun Render(
        component: Component,
        actions: ComponentActions,
        modifier: Modifier
    ) {
        val header = component as? AppMovieDetailHeaderComponent ?: return
        val uriHandler = LocalUriHandler.current
        val snackbarHostState = LocalSnackbarHostState.current
        val scope = rememberCoroutineScope()

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
                watchUrl = it.appUrl
            )
        }

        AppMovieDetailHeader(
            movie = movieItem,
            providers = providers,
            onProviderClick = { provider ->
                val providerComponent = header.providers.find { it.name == provider.name }
                try {
                    UriLauncher.openWithFallback(
                        uriHandler = uriHandler,
                        appUrl = providerComponent?.appUrl,
                        webUrl = providerComponent?.webUrl,
                        tmdbUrl = providerComponent?.tmdbUrl
                    )
                } catch (e: UriResolutionException) {
                    getLogger().d(TAG_SCREEN_ERROR,e.message.orEmpty())
                    scope.launch {
                        val message = getString(Res.string.error_provider_link)
                        snackbarHostState.showSnackbar(message)
                    }
                }
            },
            isFavorite = header.isFavorite,
            isInList = header.isInList,
            onFavoriteClick = { /* TODO: Implement action */ },
            onAddToListClick = { /* TODO: Implement action */ },
            modifier = modifier
        )
    }
}
