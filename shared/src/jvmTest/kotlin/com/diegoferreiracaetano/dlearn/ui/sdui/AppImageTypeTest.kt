package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppImageTypeTest {

    @Test
    fun `AppImageType values are correct`() {
        assertEquals("SEARCH", AppImageType.SEARCH.name)
        assertEquals("WATCHLIST", AppImageType.WATCHLIST.name)
        assertEquals("UNKNOWN", AppImageType.UNKNOWN.name)
    }
}
