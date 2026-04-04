package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertNotNull

class AppImageTypeTest {

    @Test
    fun `AppImageType values exist`() {
        AppImageType.entries.forEach { 
            assertNotNull(it.name)
        }
    }
}
