package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull
import kotlin.test.assertTrue

class AppEpisodeComponentTest {

    @Test
    fun `AppEpisodeComponent holds values`() {
        val component = AppEpisodeComponent(
            id = "1",
            title = "Episode 1",
            description = "Description",
            imageUrl = "image.png",
            duration = "45m",
            isPremium = true,
            actionUrl = "url"
        )

        assertEquals("1", component.id)
        assertEquals("Episode 1", component.title)
        assertEquals("Description", component.description)
        assertEquals("image.png", component.imageUrl)
        assertEquals("45m", component.duration)
        assertTrue(component.isPremium)
        assertEquals("url", component.actionUrl)
    }

    @Test
    fun `AppEpisodeComponent defaults`() {
        val component = AppEpisodeComponent(
            id = "1",
            title = "Title",
            description = "Desc",
            imageUrl = "",
            duration = "30m"
        )
        assertFalse(component.isPremium)
        assertNull(component.actionUrl)
    }
}
