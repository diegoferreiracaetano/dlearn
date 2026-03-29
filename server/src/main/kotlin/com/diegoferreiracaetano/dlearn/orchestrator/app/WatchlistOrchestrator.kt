package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.WATCHLIST
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.WatchlistScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.mapLatest
import org.koin.core.component.KoinComponent

class WatchlistOrchestrator(
    private val watchlistScreenBuilder: WatchlistScreenBuilder,
    private val watchlistRepository: WatchlistRepository,
    private val videoMapper: VideoMapper,
    private val tmdbClient: TmdbClient
) : Orchestrator, KoinComponent {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun execute(request: AppRequest, header: AppHeader): Flow<Screen> {
        val movieId = request.params?.get(AppQueryParam.ID)
        val isToggleAction = request.params?.containsKey(WATCHLIST) == true
        val mediaTypeString = request.params?.get(AppQueryParam.MEDIA_TYPE)

        return flow {
            if (movieId != null && isToggleAction) {
                val mediaType = mediaTypeString?.let { MediaType.valueOf(it) }
                    ?: throw IllegalArgumentException("mediaType is required for watchlist action")
                val active = request.params?.get(WATCHLIST)?.toBoolean() ?: false
                addToWatchlist(movieId, mediaType, active, header)
            }
            emitAll(getWatchlistList(header))
        }
    }

    suspend fun addToWatchlist(movieId: String, mediaType: MediaType, active: Boolean, header: AppHeader) {
        val userId = header.userId
        watchlistRepository.addToWatchlist(
            userId = userId,
            mediaId = movieId.toInt(),
            mediaType = mediaType,
            watchlist = active
        )
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun getWatchlistList(header: AppHeader): Flow<Screen> {
        val language = header.language
        val userId = header.userId

        if (userId == "guest") {
            return flow {
                emit(watchlistScreenBuilder.build(language, emptyList()))
            }
        }

        return watchlistRepository.getWatchlist(userId, language).mapLatest { watchlistItems ->
            val videos = coroutineScope {
                watchlistItems.map { (id, mediaType) ->
                    async {
                        runCatching {
                            if (mediaType == MediaType.MOVIES) {
                                tmdbClient.getMovieDetail(id.toString(), language).toVideo(mediaType)
                            } else {
                                tmdbClient.getTvShowDetail(id.toString(), language).toVideo(mediaType)
                            }
                        }.getOrNull()
                    }
                }.awaitAll().filterNotNull()
            }

            val items = videoMapper.toMovieItemComponents(videos)
            watchlistScreenBuilder.build(language, items)
        }
    }
}
