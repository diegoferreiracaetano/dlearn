package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.GetSearchDataUseCase
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
) : Orchestrator {

    override fun execute(
        request: AppRequest,
        userId: String,
        lang: String,
        appVersion: Int
    ): Flow<Screen> {
        val query = request.params?.get("q")
        return if (query != null) {
            searchContent(userId, lang, query)
        } else {
            searchMain(userId, lang)
        }
    }

    private fun searchMain(userId: String, lang: String): Flow<Screen> = flow {
        val homeData = getHomeDataUseCase.execute(userId)
        val popularItems = videoMapper.toMovieItemComponents(homeData.popular)
        emit(searchScreenBuilder.buildMain(lang, popularItems))
    }

    private fun searchContent(
        userId: String,
        lang: String,
        query: String
    ): Flow<Screen> = flow {
        val videos = getSearchDataUseCase.execute(query)
        val results = videoMapper.toMovieItemComponents(videos)
        emit(searchScreenBuilder.buildContent(query, results, lang))
    }
}
