package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.FavoriteScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class FavoriteOrchestrator(
    private val favoriteScreenBuilder: FavoriteScreenBuilder,
    private val favoriteRepository: FavoriteRepository,
    private val videoMapper: VideoMapper,
    private val tmdbClient: TmdbClient
) {
    suspend fun getFavorite(userId: String, lang: String): Screen = coroutineScope {
        val favoriteIds = favoriteRepository.getFavorites(userId)
        
        val videos = favoriteIds.map { id ->
            async {
                runCatching {
                    // In a production app we should store the media type or try both
                    tmdbClient.getMovieDetail(id).toVideo(MediaType.MOVIE) 
                }.getOrNull()
            }
        }.awaitAll().filterNotNull()

        val items = videoMapper.toMovieItemComponents(videos)
        favoriteScreenBuilder.build(lang, items)
    }

    suspend fun toggleFavorite(userId: String, movieId: String, lang: String): Screen {
        favoriteRepository.toggleFavorite(userId, movieId)
        return getFavorite(userId, lang)
    }
}
