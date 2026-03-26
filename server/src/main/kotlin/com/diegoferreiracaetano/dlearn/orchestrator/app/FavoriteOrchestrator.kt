package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
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
    private val tmdbClient: TmdbClient
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val language = header.language
        val userId = header.userId
        val movieId = request.params?.get(AppQueryParam.ID)
        
        return if (movieId != null) {
            toggleFavorite(userId, movieId, language)
        } else {
            getFavorite(userId, language)
        }
    }

    private fun getFavorite(userId: String, lang: String): Flow<Screen> = flow {
        coroutineScope {
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

    private fun toggleFavorite(userId: String, movieId: String, lang: String): Flow<Screen> = flow {
        favoriteRepository.toggleFavorite(userId, movieId)
        getFavorite(userId, lang).collect {
            emit(it)
        }
    }
}
