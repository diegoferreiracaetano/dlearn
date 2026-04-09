package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class BottomNavigationComponentTest {

    @Test
    fun `BottomNavigationComponent and BottomNavItem hold values`() {
        val item = BottomNavItem("Label", "url", AppIconType.HOME)
        val component = BottomNavigationComponent(
            items = listOf(item),
            selectedActionUrl = "url"
        )

        assertEquals(listOf(item), component.items)
        assertEquals("url", component.selectedActionUrl)
        assertEquals("Label", item.label)
        assertEquals("url", item.actionUrl)
        assertEquals(AppIconType.HOME, item.icon)
    }

    @Test
    fun `BottomNavigationComponent defaults`() {
        val component = BottomNavigationComponent(items = emptyList())
        assertNull(component.selectedActionUrl)
    }

    @Test
    fun `BottomNavItem defaults`() {
        val item = BottomNavItem("Label", "url")
        assertNull(item.icon)
    }
}
