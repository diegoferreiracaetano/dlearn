package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppListComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSearchBarComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSectionTitleComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchScreenBuilderTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = SearchScreenBuilder(i18n)

    @Test
    fun `given empty popular items when buildMain is called should return screen with empty search bar`() {
        val screen = builder.buildMain("en", emptyList())

        assertEquals(1, screen.components.size)
        val searchBar = screen.components.first() as AppSearchBarComponent
        assertTrue(searchBar.components.isEmpty())
    }

    @Test
    fun `given popular items when buildMain is called should return screen with section title and list`() {
        val items = listOf(mockk<Component>())
        val screen = builder.buildMain("en", items)

        val searchBar = screen.components.first() as AppSearchBarComponent
        assertEquals(2, searchBar.components.size)
        assertTrue(searchBar.components[0] is AppSectionTitleComponent)
        assertTrue(searchBar.components[1] is AppListComponent)
    }

    @Test
    fun `given results when buildContent is called should return screen with list component`() {
        val results = listOf(mockk<Component>())
        val screen = builder.buildContent("action", results, "en")

        val searchBar = screen.components.first() as AppSearchBarComponent
        assertEquals(1, searchBar.components.size)
        assertTrue(searchBar.components.first() is AppListComponent)
    }

    @Test
    fun `given empty results when buildContent is called should return screen with empty state component`() {
        val screen = builder.buildContent("action", emptyList(), "en")

        val searchBar = screen.components.first() as AppSearchBarComponent
        assertEquals(1, searchBar.components.size)
        assertTrue(searchBar.components.first() is AppEmptyStateComponent)
    }

    @Test
    fun `given a query when buildContent is called should set query in search bar`() {
        val screen = builder.buildContent("batman", emptyList(), "en")

        val searchBar = screen.components.first() as AppSearchBarComponent
        assertEquals("batman", searchBar.query)
    }
}
