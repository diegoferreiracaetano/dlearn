package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ProfileRowComponentTest {

    @Test
    fun `ProfileRowComponent holds values`() {
        val component = ProfileRowComponent(
            name = "Name",
            email = "Email",
            imageUrl = "image",
            editActionUrl = "edit"
        )
        assertEquals("Name", component.name)
        assertEquals("Email", component.email)
        assertEquals("image", component.imageUrl)
        assertEquals("edit", component.editActionUrl)
    }

    @Test
    fun `ProfileRowComponent defaults`() {
        val component = ProfileRowComponent("Name", "Email")
        assertNull(component.imageUrl)
        assertNull(component.editActionUrl)
    }
}
