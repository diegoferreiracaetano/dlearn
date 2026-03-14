package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ComponentIds
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class FavoriteScreenBuilder(
    private val mapper: HomeMapper,
    private val i18n: I18nProvider
) {
    fun build(
        data: HomeDomainData,
        lang: String
    ): Screen {
        val components = mutableListOf<Component>()

        if (data.popular.isNotEmpty()) {
            components.add(mapper.toCarousel(i18n.getString(AppStringType.SECTION_POPULAR, lang), data.popular))
        }

        return Screen(
            id = ComponentIds.FAVORITE_SCREEN,
            components = components,
            showSearch = true
        )
    }
}
