package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.GetSearchDataUseCase
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.SearchScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchOrchestrator(
    private val getSearchDataUseCase: GetSearchDataUseCase,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val videoMapper: VideoMapper,
    private val searchScreenBuilder: SearchScreenBuilder,
) : Orchestrator {
    override fun execute(
        request: AppRequest,
        header: AppHeader,
        userId: String,
    ): Flow<Screen> {
        val query = request.params?.get("q")

        return if (!query.isNullOrBlank()) {
            searchContent(header, query)
        } else {
            searchMain(header, userId)
        }
    }

    private fun searchMain(
        appHeader: AppHeader,
        userId: String,
    ): Flow<Screen> =
        flow {
            val homeData = getHomeDataUseCase.execute(appHeader.language, userId = userId)
            val popularItems =
                videoMapper.toMovieItemComponents(homeData.popular, appHeader.language)
            emit(searchScreenBuilder.buildMain(appHeader.language, popularItems))
        }

    private fun searchContent(
        appHeader: AppHeader,
        query: String,
    ): Flow<Screen> =
        flow {
            val videos = getSearchDataUseCase.execute(query, appHeader.language)
            val results = videoMapper.toMovieItemComponents(videos, appHeader.language)
            emit(searchScreenBuilder.buildContent(query, results, appHeader.language))
        }
}
