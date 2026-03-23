package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.network.AppUserAgent
import com.diegoferreiracaetano.dlearn.ui.screens.MovieDetailScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes


class MovieDetailOrchestrator(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val screenBuilder: MovieDetailScreenBuilder
) : Orchestrator {
    private val detailCache = InMemoryCache<String, Screen>(5.minutes)

    override fun execute(
        request: AppRequest,
        userId: String,
        userAgent: AppUserAgent
    ): Flow<Screen> = flow {
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
            ?: throw IllegalArgumentException("MovieId missing")

        val screen = detailCache.getOrPut("$movieId-${userAgent.appVersion}-${userAgent.language}") {
            val domainData = getMovieDetailUseCase.execute(movieId)
            screenBuilder.build(domainData, userAgent.language)
        }
        emit(screen)
    }
}
