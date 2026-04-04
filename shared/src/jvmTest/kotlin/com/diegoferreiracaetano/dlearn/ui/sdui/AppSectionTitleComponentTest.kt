package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppSectionTitleComponentTest {

    @Test
    fun `AppSectionTitleComponent holds values`() {
        val component = AppSectionTitleComponent("Title")
        assertEquals("Title", component.title)
    }
}
