package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppTopBarListComponentTest {

    @Test
    fun `AppTopBarListComponent and AppTopBarItem hold values`() {
        val topBar = AppTopBarComponent(title = "Title")
        val item = AppTopBarItem(topBar, "url")
        val component = AppTopBarListComponent(
            items = listOf(item),
            selectedActionUrl = "url"
        )
        
        assertEquals(listOf(item), component.items)
        assertEquals("url", component.selectedActionUrl)
        assertEquals(topBar, item.topBar)
        assertEquals("url", item.actionUrl)
    }
}
