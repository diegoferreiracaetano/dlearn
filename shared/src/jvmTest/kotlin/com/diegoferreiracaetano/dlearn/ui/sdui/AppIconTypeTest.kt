package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppIconTypeTest {

    @Test
    fun `AppIconType values are correct`() {
        assertEquals("HOME", AppIconType.HOME.name)
        assertEquals("SEARCH", AppIconType.SEARCH.name)
        assertEquals("UNKNOWN", AppIconType.UNKNOWN.name)
    }
}
