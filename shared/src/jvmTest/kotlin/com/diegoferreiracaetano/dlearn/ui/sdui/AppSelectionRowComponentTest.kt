package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AppSelectionRowComponentTest {

    @Test
    fun `AppSelectionRowComponent holds values`() {
        val component = AppSelectionRowComponent(
            title = "Title",
            preferenceKey = "key",
            value = "value",
            isSelected = true
        )
        assertEquals("Title", component.title)
        assertEquals("key", component.preferenceKey)
        assertEquals("value", component.value)
        assertTrue(component.isSelected)
    }

    @Test
    fun `AppSelectionRowComponent defaults`() {
        val component = AppSelectionRowComponent("Title", "Key", "Value")
        assertFalse(component.isSelected)
    }
}
