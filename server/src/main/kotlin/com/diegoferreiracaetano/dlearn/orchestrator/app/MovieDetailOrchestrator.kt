package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.MovieDetailScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MovieDetailOrchestrator(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val screenBuilder: MovieDetailScreenBuilder
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        header: AppHeader
    ): Flow<Screen> {
        val movieId = request.params?.get(AppQueryParam.ID)
            ?: throw IllegalArgumentException("MovieId missing")

        return flow {
            val language = header.language
            val domainData = getMovieDetailUseCase.execute(movieId, language, header)
            val screen = screenBuilder.build(domainData, language)
            emit(screen)
        }
    }
}
