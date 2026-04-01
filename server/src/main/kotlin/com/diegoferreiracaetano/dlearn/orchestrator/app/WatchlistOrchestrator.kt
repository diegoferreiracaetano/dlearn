package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.WATCHLIST
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.WatchlistScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class WatchlistOrchestrator(
    private val watchlistScreenBuilder: WatchlistScreenBuilder,
    private val watchlistRepository: WatchlistRepository,
    private val videoMapper: VideoMapper,
    private val movieClient: MovieClient
) : Orchestrator, KoinComponent {

    override fun execute(
        request: AppRequest, 
        header: AppHeader,
        userId: String
    ): Flow<Screen> = flow {
        val movieId = request.params?.get(AppQueryParam.ID)
        val isToggleAction = request.params?.containsKey(WATCHLIST) == true

        if (movieId != null && isToggleAction) {
            val active = request.params?.get(WATCHLIST)?.toBoolean() ?: false
            watchlistRepository.toggleWatchlist(userId, movieId, active)
        }

        emit(getWatchlistScreen(userId, header.language))
    }

    private suspend fun getWatchlistScreen(userId: String, language: String): Screen {
        val watchlistIds = watchlistRepository.getWatchlist(userId)
        
        val videos = coroutineScope {
            watchlistIds.map { id ->
                async {
                    runCatching {
                        movieClient.getMovieDetail(id, language)
                    }.getOrNull()
                }
            }.awaitAll().filterNotNull().map { it.toVideo() }
        }

        val items = videoMapper.toMovieItemComponents(videos)
        return watchlistScreenBuilder.build(language, items)
    }
}
