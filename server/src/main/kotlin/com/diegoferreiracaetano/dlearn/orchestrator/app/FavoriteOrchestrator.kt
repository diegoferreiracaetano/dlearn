package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAVORITE
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.FavoriteScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent

class FavoriteOrchestrator(
    private val favoriteScreenBuilder: FavoriteScreenBuilder,
    private val favoriteRepository: FavoriteRepository,
    private val videoMapper: VideoMapper,
    private val tmdbClient: TmdbClient
) : Orchestrator, KoinComponent {

    override fun execute(request: AppRequest, header: AppHeader): Flow<Screen> = flow {
        val userId = header.userId ?: throw IllegalStateException("User ID is required")
        val movieId = request.params?.get(AppQueryParam.ID)?.toIntOrNull()
        val mediaTypeString = request.params?.get(AppQueryParam.MEDIA_TYPE)
        val isToggleAction = request.params?.containsKey(FAVORITE) == true

        if (movieId != null && mediaTypeString != null && isToggleAction) {
            val mediaType = MediaType.valueOf(mediaTypeString)
            val active = request.params?.get(FAVORITE)?.toBoolean() ?: false
            favoriteRepository.toggleFavorite(userId, movieId, mediaType, active)
        }

        emit(getFavoriteScreen(userId, header.language))
    }

    private suspend fun getFavoriteScreen(userId: String, language: String): Screen {
        val favoriteItems = favoriteRepository.getFavorites(userId)
        
        val videos = coroutineScope {
            favoriteItems.map { (id, mediaType) ->
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
        return favoriteScreenBuilder.build(language, items)
    }
}
