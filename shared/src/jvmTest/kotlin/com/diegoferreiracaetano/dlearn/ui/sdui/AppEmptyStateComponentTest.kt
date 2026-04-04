package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull

class AppEmptyStateComponentTest {

    @Test
    fun `AppEmptyStateComponent holds values`() {
        val component = AppEmptyStateComponent(
            title = "Empty",
            description = "Nothing here",
            image = AppImageType.WATCHLIST
        )
        assertEquals("Empty", component.title)
        assertEquals("Nothing here", component.description)
        assertEquals(AppImageType.WATCHLIST, component.image)
    }

    @Test
    fun `AppEmptyStateComponent copy works`() {
        val component = AppEmptyStateComponent("Title", "Desc")
        val copy = component.copy(title = "New Title")
        
        assertEquals("New Title", copy.title)
        assertEquals("Desc", copy.description)
        assertNotSame(component, copy)
    }

    @Test
    fun `AppEmptyStateComponent equals and hashCode`() {
        val c1 = AppEmptyStateComponent("T", "D")
        val c2 = AppEmptyStateComponent("T", "D")
        val c3 = AppEmptyStateComponent("X", "D")
        
        assertEquals(c1, c2)
        assertEquals(c1.hashCode(), c2.hashCode())
        assertEquals(false, c1.equals(c3))
    }
}
