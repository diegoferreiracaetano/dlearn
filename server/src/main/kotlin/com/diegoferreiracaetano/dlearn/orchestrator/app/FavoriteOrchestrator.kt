package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute.FAVORITE
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
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
    private val movieClient: MovieClient
) : Orchestrator, KoinComponent {

    override fun execute(
        request: AppRequest, 
        header: AppHeader,
        userId: String
    ): Flow<Screen> = flow {
        val movieId = request.params?.get(AppQueryParam.ID)
        val isToggleAction = request.params?.containsKey(FAVORITE) == true

        if (movieId != null && isToggleAction) {
            val active = request.params?.get(FAVORITE)?.toBoolean() ?: false
            favoriteRepository.toggleFavorite(userId, movieId, active)
        }

        emit(getFavoriteScreen(userId, header.language))
    }

    private suspend fun getFavoriteScreen(userId: String, language: String): Screen {
        val favoriteIds = favoriteRepository.getFavorites(userId)
        
        val videos = coroutineScope {
            favoriteIds.map { id ->
                async {
                    runCatching {
                        movieClient.getMovieDetail(id, language)
                    }.getOrNull()
                }
            }.awaitAll().filterNotNull().map { it.toVideo() }
        }

        val items = videoMapper.toMovieItemComponents(videos)
        return favoriteScreenBuilder.build(language, items)
    }
}
