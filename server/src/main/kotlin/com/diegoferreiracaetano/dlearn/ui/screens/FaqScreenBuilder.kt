package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqItem
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppHtmlTextComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

class FaqScreenBuilder {
    fun build(faqItem: FaqItem): Screen {
        val components = mutableListOf<Component>()

        val topbar =
            AppTopBarComponent(
                title = faqItem.title,
            )

        components.add(
            AppHtmlTextComponent(
                html = faqItem.content,
            ),
        )

        return Screen(
            components =
                listOf(
                    AppContainerComponent(
                        topBar = topbar,
                        components = components,
                    ),
                ),
        )
    }
}
