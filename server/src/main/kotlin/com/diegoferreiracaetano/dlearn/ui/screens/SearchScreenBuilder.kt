package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ComponentIds
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class SearchScreenBuilder(private val i18n: I18nProvider) {
    fun build(
        query: String,
        results: List<Component>,
        appVersion: Int,
        lang: String
    ): Screen {
        val content = if (query.isBlank()) {
            emptyList()
        } else {
            results.ifEmpty {
                val emptyTitle = i18n.getString(AppStringType.SEARCH_EMPTY_TITLE, lang)
                val emptyDescriptionBase = i18n.getString(AppStringType.SEARCH_EMPTY_DESCRIPTION, lang)
                val fullDescription = "$emptyDescriptionBase \"$query\""
                
                listOf(
                    AppEmptyStateComponent(
                        title = emptyTitle,
                        description = fullDescription,
                        image = AppImageType.SEARCH
                    ) as Component
                )
            }
        }

        val placeholder = i18n.getString(AppStringType.SEARCH_PLACEHOLDER, lang)

        return Screen(
            id = ComponentIds.SEARCH_SCREEN,
            components = listOf(
                AppSearchBarComponent(
                    query = query,
                    placeholder = placeholder,
                    components = if (content.isNotEmpty()) {
                        listOf(AppListComponent(components = content) as Component)
                    } else {
                        emptyList()
                    }
                ) as Component
            )
        )
    }
}
