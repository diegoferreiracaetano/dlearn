package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.infrastructure.services.FaqItem
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppHtmlTextComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppTopBarComponent
import org.junit.Test
import kotlin.test.assertEquals

class FaqScreenBuilderTest {

    @Test
    fun `given a faq item when build is called should return screen with correct title and content`() {
        val builder = FaqScreenBuilder()
        val faqItem = FaqItem(title = "Duvida", content = "<p>Resposta</p>")
        
        val screen = builder.build(faqItem)
        
        val container = screen.components.first() as AppContainerComponent
        val topBar = container.topBar as AppTopBarComponent
        val htmlText = container.components.first() as AppHtmlTextComponent
        
        assertEquals("Duvida", topBar.title)
        assertEquals("<p>Resposta</p>", htmlText.html)
    }
}
