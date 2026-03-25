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
    private val searchScreenBuilder: SearchScreenBuilder
) {

    fun searchMain(appHeader: AppHeader): Flow<Screen> = flow {
        val homeData = getHomeDataUseCase.execute(appHeader.language)
        val popularItems = videoMapper.toMovieItemComponents(homeData.popular)
        emit(searchScreenBuilder.buildMain(appHeader.language, popularItems))
    }

    fun searchContent(
        appHeader: AppHeader,
        query: String
    ): Flow<Screen> = flow {
        val videos = getSearchDataUseCase.execute(query, appHeader.language)
        val results = videoMapper.toMovieItemComponents(videos)
        emit(searchScreenBuilder.buildContent(query, results, appHeader.language))
    }
}
