package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class WatchlistScreenBuilder(private val i18n: I18nProvider) {
    fun build(lang: String, items: List<Component>): Screen {
        val title = i18n.getString(AppStringType.NAV_WATCHLIST, lang)
        
        val components = if (items.isEmpty()) {
            listOf(
                AppEmptyStateComponent(
                    title = i18n.getString(AppStringType.WATCHLIST_EMPTY_TITLE, lang),
                    description = i18n.getString(AppStringType.WATCHLIST_EMPTY_DESCRIPTION, lang),
                    image = AppImageType.WATCHLIST
                ) as Component
            )
        } else {
            listOf(AppListComponent(components = items) as Component)
        }

        return Screen(
            id = "watchlist",
            topBar = AppTopBarComponent(
                title = title,
                showSearch = true
            ),
            components = components
        )
    }
}
