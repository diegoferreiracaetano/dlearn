package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class WatchlistScreenBuilder(private val i18n: I18nProvider) {
    fun build(lang: String, items: List<Component>): Screen {
        val title = i18n.getString(AppStringType.NAV_WATCHLIST, lang)
        
        val components = mutableListOf<Component>()

        components.add(
            AppTopBarComponent(
                title = title
            )
        )

        if (items.isEmpty()) {
            components.add(
                AppEmptyStateComponent(
                    title = i18n.getString(AppStringType.WATCHLIST_EMPTY_TITLE, lang),
                    description = i18n.getString(AppStringType.WATCHLIST_EMPTY_DESCRIPTION, lang),
                    image = AppImageType.WATCHLIST
                )
            )
        } else {
            components.add(AppListComponent(components = items))
        }

        return Screen(
            components = components
        )
    }
}
