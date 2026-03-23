package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.ui.screens.MovieDetailScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.AppRequestContext
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
        userAgent: String
    ): Flow<Screen> = flow {
        val context = AppRequestContext.fromUserAgent(userAgent)
        val movieId = request.params?.get(NavigationRoutes.MOVIE_ID_ARG)
            ?: throw IllegalArgumentException("MovieId missing")

        val screen = detailCache.getOrPut("$movieId-${context.appVersion}-${context.lang}") {
            val domainData = getMovieDetailUseCase.execute(movieId)
            screenBuilder.build(domainData, context.appVersion, context.lang)
        }
        emit(screen)
    }
}
