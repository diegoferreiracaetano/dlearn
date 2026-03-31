package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
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

class MovieDetailOrchestrator(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val screenBuilder: MovieDetailScreenBuilder,
    private val favoriteRepository: FavoriteRepository,
    private val watchlistRepository: WatchlistRepository
) : Orchestrator, KoinComponent {

    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String
    ): Flow<Screen> {
        val movieIdString = request.params?.get(AppQueryParam.ID)
            ?: throw IllegalArgumentException("MovieId missing")
        
        val movieId = movieIdString.toIntOrNull() ?: throw IllegalArgumentException("Invalid MovieId")

        val mediaTypeString = request.params?.get(AppQueryParam.MEDIA_TYPE)
        val isFavoriteAction = request.params?.containsKey(FAVORITE) == true
        val isWatchlistAction = request.params?.containsKey(WATCHLIST) == true

        return flow {
            val language = header.language

            val mediaType = mediaTypeString?.let { 
                runCatching { MediaType.valueOf(it) }.getOrNull() 
            } ?: MediaType.MOVIES

            if (isFavoriteAction) {
                val active = request.params?.get(FAVORITE)?.toBoolean() ?: false
                favoriteRepository.toggleFavorite(userId, movieId, mediaType, active)
            }

            if (isWatchlistAction) {
                val active = request.params?.get(WATCHLIST)?.toBoolean() ?: false
                watchlistRepository.toggleWatchlist(userId, movieId, mediaType, active)
            }

            // Busca os dados (agora com o estado de favorito/watchlist atualizado no nosso banco)
            val domainData = getMovieDetailUseCase.execute(movieIdString, language, userId)
            emit(screenBuilder.build(domainData, language))
        }
    }
}
