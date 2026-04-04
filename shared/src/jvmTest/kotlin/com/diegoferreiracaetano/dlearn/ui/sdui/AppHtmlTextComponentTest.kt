package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppHtmlTextComponentTest {

    @Test
    fun `AppHtmlTextComponent holds values`() {
        val component = AppHtmlTextComponent("<html><body>Test</body></html>")
        assertEquals("<html><body>Test</body></html>", component.html)
    }
}
