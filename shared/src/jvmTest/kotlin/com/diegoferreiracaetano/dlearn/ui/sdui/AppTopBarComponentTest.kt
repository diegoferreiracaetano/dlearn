package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppTopBarComponentTest {

    @Test
    fun `AppTopBarComponent holds values`() {
        val component = AppTopBarComponent(
            title = "Title",
            subtitle = "Subtitle",
            imageUrl = "image",
            showSearch = true
        )
        assertEquals("Title", component.title)
        assertEquals("Subtitle", component.subtitle)
        assertEquals("image", component.imageUrl)
        assertTrue(component.showSearch)
    }

    @Test
    fun `AppTopBarComponent defaults`() {
        val component = AppTopBarComponent("Title")
        assertNull(component.subtitle)
        assertNull(component.imageUrl)
        assertFalse(component.showSearch)
    }
}
