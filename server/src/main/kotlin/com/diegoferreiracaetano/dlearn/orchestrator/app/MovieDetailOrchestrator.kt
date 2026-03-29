package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAVORITE
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.WATCHLIST
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.MovieDetailScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class MovieDetailOrchestrator(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val screenBuilder: MovieDetailScreenBuilder
) : Orchestrator, KoinComponent {

    private val favoriteOrchestrator: FavoriteOrchestrator by inject()
    private val watchlistOrchestrator: WatchlistOrchestrator by inject()

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val movieId = request.params?.get(AppQueryParam.ID)
            ?: throw IllegalArgumentException("MovieId missing")

        val mediaTypeString = request.params?.get(AppQueryParam.MEDIA_TYPE)
        val isFavoriteAction = request.params?.containsKey(FAVORITE) == true
        val isWatchlistAction = request.params?.containsKey(WATCHLIST) == true

        return flow {
            val language = header.language

            // Se for uma ação de toggle (favoritar ou adicionar à lista), executamos a ação antes de buscar o estado atual
            val mediaType = mediaTypeString?.let { 
                runCatching { MediaType.valueOf(it) }.getOrNull() 
            } ?: MediaType.MOVIES

            if (isFavoriteAction) {
                val active = request.params?.get(FAVORITE)?.toBoolean() ?: false
                favoriteOrchestrator.markAsFavorite(movieId, mediaType, active, header)
            }

            if (isWatchlistAction) {
                val active = request.params?.get(WATCHLIST)?.toBoolean() ?: false
                watchlistOrchestrator.addToWatchlist(movieId, mediaType, active, header)
            }

            // Busca os dados (agora com o estado de favorito/watchlist atualizado no TMDB)
            val domainData = getMovieDetailUseCase.execute(movieId, language, header)
            emit(screenBuilder.build(domainData, language))
        }
    }
}
