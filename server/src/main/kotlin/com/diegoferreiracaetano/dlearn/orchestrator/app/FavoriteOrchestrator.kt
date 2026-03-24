package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.toVideo
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

class FavoriteOrchestrator(
    private val favoriteScreenBuilder: FavoriteScreenBuilder,
    private val favoriteRepository: FavoriteRepository,
    private val videoMapper: VideoMapper,
    private val tmdbClient: TmdbClient,
    private val sessionManager: SessionManager
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val language = header.language
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
        
        return if (movieId != null) {
            toggleFavorite(movieId, language)
        } else {
            getFavorite(language)
        }
    }

    private fun getFavorite(lang: String): Flow<Screen> = flow {
        coroutineScope {

            val userId = sessionManager.user().id
            val favoriteIds = favoriteRepository.getFavorites(userId)

            val videos = favoriteIds.map { id ->
                async {
                    runCatching {
                        tmdbClient.getMovieDetail(id, lang).toVideo(MediaType.MOVIE)
                    }.getOrNull()
                }
            }.awaitAll().filterNotNull()

            val items = videoMapper.toMovieItemComponents(videos)
            emit(favoriteScreenBuilder.build(lang, items))
        }
    }

    private fun toggleFavorite(movieId: String, lang: String): Flow<Screen> = flow {
        val userId = sessionManager.user().id
        favoriteRepository.toggleFavorite(userId, movieId)
        getFavorite(lang).collect {
            emit(it)
        }
    }
}
