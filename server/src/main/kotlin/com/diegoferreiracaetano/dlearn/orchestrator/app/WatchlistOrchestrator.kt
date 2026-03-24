package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.WatchlistScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WatchlistOrchestrator(
    private val watchlistScreenBuilder: WatchlistScreenBuilder,
    private val watchlistRepository: WatchlistRepository,
    private val videoMapper: VideoMapper,
    private val tmdbClient: TmdbClient
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val language = header.language
        val userId = header.userId
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
        
        return if (movieId != null) {
            toggleWatchlist(userId, movieId, language)
        } else {
            getWatchlist(userId, language)
        }
    }

    private fun getWatchlist(userId: String, lang: String): Flow<Screen> = flow {
        coroutineScope {
            val watchlistIds = watchlistRepository.getWatchlist(userId)

            val videos = watchlistIds.map { id ->
                async {
                    runCatching {
                        tmdbClient.getMovieDetail(id, lang).toVideo(MediaType.MOVIE)
                    }.getOrNull()
                }
            }.awaitAll().filterNotNull()

            val items = videoMapper.toMovieItemComponents(videos)
            emit(watchlistScreenBuilder.build(lang, items))
        }
    }

    private fun toggleWatchlist(userId: String, movieId: String, lang: String): Flow<Screen> = flow {
        watchlistRepository.toggleWatchlist(userId, movieId)
        getWatchlist(userId, lang).collect {
            emit(it)
        }
    }
}