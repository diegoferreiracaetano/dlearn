package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppProfileHeaderComponentTest {

    @Test
    fun `AppProfileHeaderComponent holds values`() {
        val component = AppProfileHeaderComponent(
            name = "Name",
            email = "Email",
            imageUrl = "image",
            onImagePickedAction = "action"
        )
        assertEquals("Name", component.name)
        assertEquals("Email", component.email)
        assertEquals("image", component.imageUrl)
        assertEquals("action", component.onImagePickedAction)
    }

    @Test
    fun `AppProfileHeaderComponent defaults`() {
        val component = AppProfileHeaderComponent("Name", "Email")
        assertNull(component.imageUrl)
        assertNull(component.onImagePickedAction)
    }
}
