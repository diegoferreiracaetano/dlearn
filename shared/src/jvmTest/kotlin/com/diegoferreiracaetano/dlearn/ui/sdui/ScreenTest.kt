package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class ScreenTest {

    @Test
    fun `Screen holds components`() {
        val components = listOf<Component>(AppLoadingComponent)
        val screen = Screen(components)
        assertEquals(components, screen.components)
    }

    @Test
    fun `Screen default is empty`() {
        val screen = Screen()
        assertEquals(emptyList(), screen.components)
    }
}
