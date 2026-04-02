package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.sdui.AppEmptyStateComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class WatchlistScreenBuilderTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = WatchlistScreenBuilder(i18n)

    @Test
    fun `given empty items when build is called should return screen with empty state component`() {
        val screen = builder.build("en", emptyList())

        assertEquals(1, screen.components.size)
        assertTrue(screen.components.first() is AppEmptyStateComponent)
    }

    @Test
    fun `given a list of items when build is called should return screen containing those items`() {
        val items = listOf(mockk<Component>(), mockk<Component>())

        val screen = builder.build("en", items)

        assertEquals(2, screen.components.size)
        assertEquals(items, screen.components)
    }
}
