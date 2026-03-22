package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.ui.screens.MovieDetailScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes

interface MovieDetailOrchestrator {
    fun handleRequest(request: AppRequest, appVersion: Int, lang: String): Flow<Screen>
}

class MovieDetailOrchestratorImpl(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val screenBuilder: MovieDetailScreenBuilder
) : MovieDetailOrchestrator {
    private val detailCache = InMemoryCache<String, Screen>(5.minutes)

    override fun handleRequest(request: AppRequest, appVersion: Int, lang: String): Flow<Screen> = flow {
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
            ?: throw IllegalArgumentException("MovieId missing")

        val screen = detailCache.getOrPut("$movieId-$appVersion-$lang") {
            val domainData = getMovieDetailUseCase.execute(movieId)
            screenBuilder.build(domainData, appVersion, lang)
        }
        emit(screen)
    }
}
