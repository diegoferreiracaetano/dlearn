package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotSame
import kotlin.test.assertNull

class AppContainerComponentTest {

    @Test
    fun `AppContainerComponent holds values`() {
        val topBar = AppTopBarComponent(title = "Top")
        val searchBar = AppSearchBarComponent(placeholder = "Search")
        val chipGroup = ChipGroupComponent(items = emptyList(), cleanUrl = "url")
        val bottomBar = BottomNavigationComponent(items = emptyList())
        val components = listOf<Component>(AppLoadingComponent)

        val container = AppContainerComponent(
            topBar = topBar,
            searchBar = searchBar,
            chipGroup = chipGroup,
            bottomBar = bottomBar,
            components = components
        )

        assertEquals(topBar, container.topBar)
        assertEquals(searchBar, container.searchBar)
        assertEquals(chipGroup, container.chipGroup)
        assertEquals(bottomBar, container.bottomBar)
        assertEquals(components, container.components)
    }

    @Test
    fun `AppContainerComponent defaults`() {
        val container = AppContainerComponent()
        assertNull(container.topBar)
        assertNull(container.searchBar)
        assertNull(container.chipGroup)
        assertNull(container.bottomBar)
        assertEquals(emptyList(), container.components)
    }

    @Test
    fun `AppContainerComponent copy works`() {
        val container = AppContainerComponent()
        val topBar = AppTopBarComponent(title = "New Top")
        val copy = container.copy(topBar = topBar)

        assertEquals(topBar, copy.topBar)
        assertNull(copy.searchBar)
        assertNotSame(container, copy)
    }

    @Test
    fun `AppContainerComponent equals and hashCode`() {
        val c1 = AppContainerComponent(topBar = AppTopBarComponent(title = "T"))
        val c2 = AppContainerComponent(topBar = AppTopBarComponent(title = "T"))
        val c3 = AppContainerComponent(topBar = AppTopBarComponent(title = "X"))

        assertEquals(c1, c2)
        assertEquals(c1.hashCode(), c2.hashCode())
        assertEquals(false, c1.equals(c3))
        assertEquals(false, false)
        assertEquals(false, c1.equals("string"))
    }
}
