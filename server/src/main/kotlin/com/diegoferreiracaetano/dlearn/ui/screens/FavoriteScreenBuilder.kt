package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppImageType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider

class FavoriteScreenBuilder(
    private val i18n: I18nProvider,
) {
    fun build(
        lang: String,
        items: List<Component>,
    ): Screen {
        val components = mutableListOf<Component>()

        if (items.isEmpty()) {
            components.add(
                AppEmptyStateComponent(
                    title = i18n.getString(AppStringType.FAVORITE_EMPTY_TITLE, lang),
                    description = i18n.getString(AppStringType.FAVORITE_EMPTY_DESCRIPTION, lang),
                    image = AppImageType.FAVORITE,
                ),
            )
        } else {
            components.addAll(items)
        }

        return Screen(
            components = components,
        )
    }
}
