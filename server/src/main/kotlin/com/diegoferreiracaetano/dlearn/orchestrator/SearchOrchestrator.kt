package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.usecases.GetSearchDataUseCase
import com.diegoferreiracaetano.dlearn.ui.screens.SearchScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.MovieItemComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import java.util.Locale

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
                rating = video.rating?.let { String.format(Locale.US, "%.1f", it) },
                year = video.subtitle,
                duration = null,
                contentRating = "L",
                genre = video.categories.firstOrNull()?.title,
                movieType = if (video.mediaType == MediaType.MOVIE) "Filme" else "Série",
                actionUrl = "/video/${video.id}"
            ) as Component
        }
        return searchScreenBuilder.buildContent(query, results, lang)
    }
}
