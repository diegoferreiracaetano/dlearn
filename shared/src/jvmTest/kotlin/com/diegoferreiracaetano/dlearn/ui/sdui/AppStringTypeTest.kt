package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertNotNull

class AppStringTypeTest {

    @Test
    fun `AppStringType values exist`() {
        AppStringType.entries.forEach {
            assertNotNull(it.name)
        }
    }
}
