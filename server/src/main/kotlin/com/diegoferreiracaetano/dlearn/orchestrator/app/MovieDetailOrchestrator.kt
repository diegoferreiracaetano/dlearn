package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
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
    private val watchlistRepository: WatchlistRepository,
) : Orchestrator,
    KoinComponent {
    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String,
    ): Flow<Screen> {
        val movieId =
            request.params?.get(AppQueryParam.ID)
                ?: throw IllegalArgumentException("MovieId missing")

        val isFavoriteAction = request.params?.containsKey(FAVORITE) == true
        val isWatchlistAction = request.params?.containsKey(WATCHLIST) == true

        return flow {
            val language = header.language

            if (isFavoriteAction) {
                val active = request.params?.get(FAVORITE)?.toBoolean() ?: false
                favoriteRepository.toggleFavorite(userId, movieId, active)
            }

            if (isWatchlistAction) {
                val active = request.params?.get(WATCHLIST)?.toBoolean() ?: false
                watchlistRepository.toggleWatchlist(userId, movieId, active)
            }

            val domainData = getMovieDetailUseCase.execute(movieId, language, userId)
            emit(screenBuilder.build(domainData, language))
        }
    }
}
