package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqItem
import com.diegoferreiracaetano.dlearn.ui.sdui.*

class FaqScreenBuilder {
    fun build(faqItem: FaqItem): Screen {
        val components = mutableListOf<Component>()

        components.add(
            AppTopBarComponent(
                title = faqItem.title
            )
        )

        components.add(
            AppHtmlTextComponent(
                html = faqItem.content
            )
        )

        return Screen(
            components = components
        )
    }
}
