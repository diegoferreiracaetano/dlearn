package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.MovieDetailMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class MovieDetailScreenBuilder(
    private val mapper: MovieDetailMapper,
) {
    fun build(
        data: MovieDetailDomainData,
        lang: String,
        selectedSeason: Int = 1,
    ): Screen {
        val components = mutableListOf<Component>()

        components.add(mapper.toHeader(data, lang))
        components.add(mapper.toStoryLine(data, lang))
        components.add(mapper.toCastCarousel(data, lang))

        components.addAll(mapper.toEpisodesSection(data, lang, selectedSeason))

        val topBar =
            AppTopBarComponent(
                title = data.title,
            )

        return Screen(
            components =
            listOf(
                AppContainerComponent(
                    topBar = topBar,
                    components = components,
                )
            )
        )
    }
}
