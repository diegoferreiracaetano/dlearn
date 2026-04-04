package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class SectionComponentTest {

    @Test
    fun `SectionComponent and SectionItem hold values`() {
        val item = SectionItem(
            id = "1",
            label = "Label",
            value = "Value",
            icon = AppIconType.HOME,
            actionUrl = "url"
        )
        val component = SectionComponent(
            title = "Section",
            items = listOf(item)
        )
        
        assertEquals("Section", component.title)
        assertEquals(listOf(item), component.items)
        assertEquals("1", item.id)
        assertEquals("Label", item.label)
        assertEquals("Value", item.value)
        assertEquals(AppIconType.HOME, item.icon)
        assertEquals("url", item.actionUrl)
    }

    @Test
    fun `SectionItem defaults`() {
        val item = SectionItem(id = "1", label = "Label")
        assertNull(item.value)
        assertNull(item.icon)
        assertNull(item.actionUrl)
    }
}
