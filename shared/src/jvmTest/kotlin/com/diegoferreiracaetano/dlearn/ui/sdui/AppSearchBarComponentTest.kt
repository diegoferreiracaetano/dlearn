package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppSearchBarComponentTest {

    @Test
    fun `AppSearchBarComponent holds values`() {
        val components = listOf<Component>(AppLoadingComponent)
        val component = AppSearchBarComponent(
            query = "Search",
            placeholder = "Placeholder",
            actionUrl = "url",
            components = components
        )
        assertEquals("Search", component.query)
        assertEquals("Placeholder", component.placeholder)
        assertEquals("url", component.actionUrl)
        assertEquals(components, component.components)
    }

    @Test
    fun `AppSearchBarComponent defaults`() {
        val component = AppSearchBarComponent()
        assertEquals("", component.query)
        assertEquals("", component.placeholder)
        assertEquals("", component.actionUrl)
        assertEquals(emptyList(), component.components)
    }
}
