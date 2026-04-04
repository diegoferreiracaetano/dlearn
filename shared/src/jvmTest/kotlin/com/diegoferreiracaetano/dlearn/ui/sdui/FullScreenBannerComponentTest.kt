package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class FullScreenBannerComponentTest {

    @Test
    fun `FullScreenBannerComponent holds values`() {
        val component = FullScreenBannerComponent(
            id = "1",
            title = "Title",
            subtitle = "Subtitle",
            imageUrl = "image",
            actionUrl = "url"
        )
        
        assertEquals("1", component.id)
        assertEquals("Title", component.title)
        assertEquals("Subtitle", component.subtitle)
        assertEquals("image", component.imageUrl)
        assertEquals("url", component.actionUrl)
    }

    @Test
    fun `FullScreenBannerComponent defaults`() {
        val component = FullScreenBannerComponent(id = "1", title = "Title", imageUrl = "image")
        assertNull(component.subtitle)
        assertNull(component.actionUrl)
    }
}
