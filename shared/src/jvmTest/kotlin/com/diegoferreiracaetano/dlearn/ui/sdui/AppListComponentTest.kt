package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class AppListComponentTest {

    @Test
    fun `AppListComponent holds components`() {
        val components = listOf<Component>(AppLoadingComponent)
        val component = AppListComponent(components)
        assertEquals(components, component.components)
    }

    @Test
    fun `AppListComponent default is empty`() {
        val component = AppListComponent()
        assertEquals(emptyList(), component.components)
    }
}
