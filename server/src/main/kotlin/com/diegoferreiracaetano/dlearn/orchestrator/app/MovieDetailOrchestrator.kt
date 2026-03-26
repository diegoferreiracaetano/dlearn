package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.network.AppHeader
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
        header: AppHeader
    ): Flow<Screen> = flow {
        val movieId = request.params?.get(AppQueryParam.ID)
            ?: throw IllegalArgumentException("MovieId missing")

        val language = header.language
        val screen = detailCache.getOrPut("$movieId-${header.userAgent.appVersion}-$language") {
            val domainData = getMovieDetailUseCase.execute(movieId, language)
            screenBuilder.build(domainData, language)
        }
        emit(screen)
    }
}
