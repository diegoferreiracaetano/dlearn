package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ComponentIds
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.ui.mappers.HomeMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.*
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

        if (data.popular.isEmpty()) {
            components.add(
                AppEmptyStateComponent(
                    title = i18n.getString(AppStringType.FAVORITE_EMPTY_TITLE, lang),
                    description = i18n.getString(AppStringType.FAVORITE_EMPTY_DESCRIPTION, lang)
                )
            )
        } else {
            components.add(mapper.toCarousel(i18n.getString(AppStringType.SECTION_POPULAR, lang), data.popular))
        }

        return Screen(
            id = ComponentIds.FAVORITE_SCREEN,
            components = listOf(
                AppContainerComponent(
                    topBar = AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_FAVORITES, lang),
                        showSearch = false
                    ),
                    components = components
                )
            )
        )
    }
}
