package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FooterComponentTest {

    @Test
    fun `FooterComponent holds values`() {
        val component = FooterComponent(
            label = "Footer",
            actionUrl = "action",
            closeUrl = "close"
        )
        assertEquals("Footer", component.label)
        assertEquals("action", component.actionUrl)
        assertEquals("close", component.closeUrl)
    }

    @Test
    fun `FooterComponent defaults`() {
        val component = FooterComponent("Label")
        assertNull(component.actionUrl)
        assertNull(component.closeUrl)
    }
}
