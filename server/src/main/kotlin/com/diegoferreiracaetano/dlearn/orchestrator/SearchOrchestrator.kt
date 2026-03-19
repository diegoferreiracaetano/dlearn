package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.usecases.GetSearchDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.SearchScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class SearchOrchestrator(
    private val getSearchDataUseCase: GetSearchDataUseCase,
    private val getHomeDataUseCase: GetHomeDataUseCase,
    private val videoMapper: VideoMapper,
    private val searchScreenBuilder: SearchScreenBuilder
) {
    suspend fun searchMain(userId: String, lang: String): Screen {
        val homeData = getHomeDataUseCase.execute(userId)
        val popularItems = videoMapper.toMovieItemComponents(homeData.popular)
        return searchScreenBuilder.buildMain(lang, popularItems)
    }

    suspend fun searchContent(
        userId: String,
        lang: String,
        query: String
    ): Screen {
        val videos = getSearchDataUseCase.execute(query)
        val results = videoMapper.toMovieItemComponents(videos)
        return searchScreenBuilder.buildContent(query, results, lang)
    }
}
