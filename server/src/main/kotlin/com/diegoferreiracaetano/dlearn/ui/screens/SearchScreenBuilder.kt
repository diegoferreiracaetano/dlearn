package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppImageType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSearchBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSectionTitleComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class SearchScreenBuilder(
    private val i18n: I18nProvider,
) {
    fun buildMain(
        lang: String,
        popularItems: List<Component> = emptyList(),
    ): Screen {
        val placeholder = i18n.getString(AppStringType.SEARCH_PLACEHOLDER, lang)
        val sectionTitle = i18n.getString(AppStringType.SECTION_POPULAR, lang)

        val searchBarComponents = mutableListOf<Component>()

        if (popularItems.isNotEmpty()) {
            searchBarComponents.add(AppSectionTitleComponent(title = sectionTitle) as Component)
            searchBarComponents.add(AppListComponent(components = popularItems) as Component)
        }

        return Screen(
            components =
            listOf(
                AppSearchBarComponent(
                    actionUrl = AppNavigationRoute.SEARCH,
                    query = Constants.EMPTY_STRING,
                    placeholder = placeholder,
                    components = searchBarComponents,
                ) as Component,
            ),
        )
    }

    fun buildContent(
        query: String,
        results: List<Component>,
        lang: String,
    ): Screen {
        val placeholder = i18n.getString(AppStringType.SEARCH_PLACEHOLDER, lang)

        val contentComponents =
            if (results.isEmpty()) {
                val emptyTitle = i18n.getString(AppStringType.SEARCH_EMPTY_TITLE, lang)
                val emptyDescription =
                    i18n.getString(AppStringType.SEARCH_EMPTY_DESCRIPTION, lang, query)

                listOf(
                    AppEmptyStateComponent(
                        title = emptyTitle,
                        description = emptyDescription,
                        image = AppImageType.SEARCH,
                    ) as Component,
                )
            } else {
                listOf(AppListComponent(components = results))
            }

        return Screen(
            components =
            listOf(
                AppSearchBarComponent(
                    actionUrl = AppNavigationRoute.SEARCH,
                    query = query,
                    placeholder = placeholder,
                    components = contentComponents,
                ),
            ),
        )
    }
}
