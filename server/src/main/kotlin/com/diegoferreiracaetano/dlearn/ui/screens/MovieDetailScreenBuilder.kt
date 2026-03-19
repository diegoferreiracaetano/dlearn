package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.MovieDetailMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
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

        val topBar = AppTopBarComponent(
            title = data.title,
        )

        return Screen(
            components = listOf(
                AppContainerComponent(
                    topBar = topBar,
                    components = components
                )
            )
        )
    }
}
