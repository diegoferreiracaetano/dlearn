package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppExpandableSectionComponentTest {

    @Test
    fun `AppExpandableSectionComponent holds values`() {
        val component = AppExpandableSectionComponent(
            title = "Title",
            text = "Text",
            maxLines = 5
        )
        assertEquals("Title", component.title)
        assertEquals("Text", component.text)
        assertEquals(5, component.maxLines)
    }

    @Test
    fun `AppExpandableSectionComponent defaults`() {
        val component = AppExpandableSectionComponent("Title", "Text")
        assertEquals(3, component.maxLines)
    }
}
