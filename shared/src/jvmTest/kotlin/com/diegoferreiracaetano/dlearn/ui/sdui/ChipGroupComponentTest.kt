package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChipGroupComponentTest {

    @Test
    fun `ChipGroupComponent and ChipItem hold values`() {
        val option = ChipItem(id = "2", label = "Option", actionUrl = "url2")
        val item = ChipItem(
            id = "1",
            label = "Label",
            isSelected = true,
            hasDropDown = true,
            isFilter = false,
            options = listOf(option),
            actionUrl = "url"
        )
        val component = ChipGroupComponent(
            items = listOf(item),
            cleanUrl = "clean"
        )
        
        assertEquals(listOf(item), component.items)
        assertEquals("clean", component.cleanUrl)
        assertEquals("1", item.id)
        assertEquals("Label", item.label)
        assertTrue(item.isSelected)
        assertTrue(item.hasDropDown)
        assertFalse(item.isFilter)
        assertEquals(listOf(option), item.options)
        assertEquals("url", item.actionUrl)
    }

    @Test
    fun `ChipItem defaults`() {
        val item = ChipItem(id = "1", label = "Label", actionUrl = "url")
        assertFalse(item.isSelected)
        assertFalse(item.hasDropDown)
        assertTrue(item.isFilter)
        assertNull(item.options)
    }
}
