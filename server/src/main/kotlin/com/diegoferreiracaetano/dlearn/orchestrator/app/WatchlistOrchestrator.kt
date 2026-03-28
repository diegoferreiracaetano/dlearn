package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
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
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class WatchlistOrchestrator(
    private val watchlistScreenBuilder: WatchlistScreenBuilder,
    private val watchlistRepository: WatchlistRepository,
    private val videoMapper: VideoMapper,
    private val tmdbClient: TmdbClient
) : Orchestrator, KoinComponent {

    private val movieDetailOrchestrator: MovieDetailOrchestrator by inject()
    private val homeOrchestrator: HomeOrchestrator by inject()

    override fun execute(request: AppRequest, header: AppHeader): Flow<Screen> {
        val movieId = request.params?.get(AppQueryParam.ID)
        val active = request.params?.get("active")?.toBoolean() ?: false
        val returnPath = request.params?.get("returnPath")

        return flow {
            if (movieId != null) {
                // Perform the action via Repository (SOLID)
                val success = watchlistRepository.addToWatchlist(
                    accountId = header.tmdbAccountId ?: "",
                    sessionId = header.tmdbSessionId ?: "",
                    mediaId = movieId.toInt(),
                    watchlist = active
                )
                
                if (success) {
                    // Decide which screen to re-render based on returnPath
                    when (returnPath) {
                        AppNavigationRoute.MOVIES -> {
                            movieDetailOrchestrator.execute(request, header).collect { emit(it) }
                            return@flow
                        }
                        AppNavigationRoute.HOME -> {
                            homeOrchestrator.execute(request, header).collect { emit(it) }
                            return@flow
                        }
                    }
                }
            }
            
            // Default behavior: return the watchlist list
            getWatchlistList(header).collect { emit(it) }
        }
    }

    private fun getWatchlistList(header: AppHeader): Flow<Screen> = flow {
        coroutineScope {
            val language = header.language
            val sessionId = header.tmdbSessionId
            val accountId = header.tmdbAccountId

            if (sessionId == null || accountId == null) {
                emit(watchlistScreenBuilder.build(language, emptyList()))
                return@coroutineScope
            }

            val watchlistIds = watchlistRepository.getWatchlist(accountId, sessionId, language)

            val videos = watchlistIds.map { id ->
                async {
                    runCatching {
                        tmdbClient.getMovieDetail(id.toString(), language).toVideo(MediaType.MOVIE)
                    }.getOrNull()
                }
            }.awaitAll().filterNotNull()

            val items = videoMapper.toMovieItemComponents(videos)
            emit(watchlistScreenBuilder.build(language, items))
        }
    }
}
