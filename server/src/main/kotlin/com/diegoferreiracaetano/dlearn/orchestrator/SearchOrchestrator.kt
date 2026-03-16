package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.usecases.GetSearchDataUseCase
import com.diegoferreiracaetano.dlearn.ui.screens.SearchScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class SearchOrchestrator(
    private val getSearchDataUseCase: GetSearchDataUseCase,
    private val searchScreenBuilder: SearchScreenBuilder
) {
    fun searchMain(lang: String): Screen {
        return searchScreenBuilder.buildMain(lang)
    }

    suspend fun searchContent(
        userId: String,
        lang: String,
        query: String
    ): Screen {
        val videos = getSearchDataUseCase.execute(query)
        val results = videos.map { video ->
             MovieItemComponent(
                id = video.id,
                title = video.title,
                subtitle = video.subtitle,
                imageUrl = video.imageUrl,
                rating = video.rating?.toString(),
                genre = video.categories.firstOrNull()?.title,
                movieType = video.mediaType.name,
                actionUrl = "/video/${video.id}"
            ) as Component
        }
        return searchScreenBuilder.buildContent(query, results, lang)
    }
}
