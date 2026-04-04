package com.diegoferreiracaetano.dlearn.ui.util

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import kotlin.test.Test
import kotlin.test.assertEquals

class JsonExtensionsTest {

    @Test
    fun `decodeToScreen decodes simple screen`() {
        val json = """{"components":[]}"""
        val screen = json.decodeToScreen()
        assertEquals(0, screen.components.size)
    }
}
