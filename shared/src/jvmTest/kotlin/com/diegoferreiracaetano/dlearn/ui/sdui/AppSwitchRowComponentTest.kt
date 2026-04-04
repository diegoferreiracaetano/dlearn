package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppSwitchRowComponentTest {

    @Test
    fun `AppSwitchRowComponent holds values`() {
        val component = AppSwitchRowComponent(
            title = "Title",
            subtitle = "Subtitle",
            icon = AppIconType.HOME,
            preferenceKey = "key",
            isChecked = true
        )
        assertEquals("Title", component.title)
        assertEquals("Subtitle", component.subtitle)
        assertEquals(AppIconType.HOME, component.icon)
        assertEquals("key", component.preferenceKey)
        assertTrue(component.isChecked)
    }

    @Test
    fun `AppSwitchRowComponent defaults`() {
        val component = AppSwitchRowComponent(title = "Title", preferenceKey = "key")
        assertNull(component.subtitle)
        assertNull(component.icon)
        assertFalse(component.isChecked)
    }
}
