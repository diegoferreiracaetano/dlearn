package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppStringTypeTest {

    @Test
    fun `AppStringType values are correct`() {
        assertEquals("PROFILE_TITLE", AppStringType.PROFILE_TITLE.name)
        assertEquals("UNKNOWN", AppStringType.UNKNOWN.name)
    }
}
