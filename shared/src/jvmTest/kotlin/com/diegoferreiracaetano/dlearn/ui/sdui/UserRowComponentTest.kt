package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserRowComponentTest {

    @Test
    fun `UserRowComponent holds values`() {
        val component = UserRowComponent(
            name = "Name",
            role = "Role",
            imageUrl = "image",
            actionUrl = "action"
        )
        assertEquals("Name", component.name)
        assertEquals("Role", component.role)
        assertEquals("image", component.imageUrl)
        assertEquals("action", component.actionUrl)
    }

    @Test
    fun `UserRowComponent defaults`() {
        val component = UserRowComponent("Name", "Role")
        assertNull(component.imageUrl)
        assertNull(component.actionUrl)
    }
}
