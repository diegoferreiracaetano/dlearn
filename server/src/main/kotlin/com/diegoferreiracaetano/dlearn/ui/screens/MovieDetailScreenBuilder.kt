package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.MovieDetailMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class MovieDetailScreenBuilder(
    private val mapper: MovieDetailMapper,
    private val i18n: I18nProvider
) {

    fun build(data: MovieDetailDomainData, appVersion: Int, lang: String): Screen {
        val components = mutableListOf<Component>()

        components.add(mapper.toHeader(data, lang))
        components.add(mapper.toStoryLine(data, lang))
        components.add(mapper.toCastCarousel(data, lang))
        
        mapper.toEpisodesSection(data, lang)?.let { 
            components.add(it) 
        }

        return Screen(
            id = "movie_detail_${data.id}",
            components = listOf(
                AppContainerComponent(
                    topBar = AppTopBarComponent(
                        title = data.title,
                        showSearch = false
                    ),
                    components = components
                )
            )
        )
    }
}
