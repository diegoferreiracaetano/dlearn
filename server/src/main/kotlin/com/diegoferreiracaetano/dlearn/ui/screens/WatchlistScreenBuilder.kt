package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ComponentIds
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class WatchlistScreenBuilder(private val i18n: I18nProvider) {
    fun build(lang: String): Screen {
        return Screen(
            id = ComponentIds.WATCHLIST_SCREEN,
            components = listOf(
                AppContainerComponent(
                    topBar = AppTopBarComponent(
                        title = i18n.getString(AppStringType.NAV_WATCHLIST, lang),
                        showSearch = false
                    ),
                    components = listOf(
                        AppEmptyStateComponent(
                            title = i18n.getString(AppStringType.WATCHLIST_EMPTY_TITLE, lang),
                            description = i18n.getString(AppStringType.WATCHLIST_EMPTY_DESCRIPTION, lang)
                        )
                    )
                )
            )
        )
    }
}
