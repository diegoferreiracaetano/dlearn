package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.infrastructure.cache.InMemoryCache
import com.diegoferreiracaetano.dlearn.ui.screens.MovieDetailScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlin.time.Duration.Companion.minutes

class MovieDetailOrchestrator(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val screenBuilder: MovieDetailScreenBuilder
) {
    private val detailCache = InMemoryCache<String, Screen>(5.minutes)

    fun getMovieDetail(movieId: String, appVersion: Int, lang: String): Flow<Screen> = flow {
        val screen = detailCache.getOrPut("$movieId-$appVersion-$lang") {
            val domainData = getMovieDetailUseCase.execute(movieId)
            screenBuilder.build(domainData, appVersion, lang)
        }
        emit(screen)
    }
}
