package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppFeedbackComponentTest {

    @Test
    fun `AppFeedbackComponent holds values`() {
        val component = AppFeedbackComponent(
            title = "Title",
            description = "Description",
            imageSource = "image",
            primaryText = "Primary",
            secondaryText = "Secondary"
        )
        assertEquals("Title", component.title)
        assertEquals("Description", component.description)
        assertEquals("image", component.imageSource)
        assertEquals("Primary", component.primaryText)
        assertEquals("Secondary", component.secondaryText)
    }

    @Test
    fun `AppFeedbackComponent defaults`() {
        val component = AppFeedbackComponent("Title", "Description")
        assertNull(component.imageSource)
        assertNull(component.primaryText)
        assertNull(component.secondaryText)
    }
}
