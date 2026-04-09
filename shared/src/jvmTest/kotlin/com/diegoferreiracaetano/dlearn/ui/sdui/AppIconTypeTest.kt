package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertNotNull

class AppIconTypeTest {

    @Test
    fun `AppIconType values exist`() {
        AppIconType.entries.forEach {
            assertNotNull(it.name)
        }
    }
}
