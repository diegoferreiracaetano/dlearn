package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class BannerCarouselComponentTest {

    @Test
    fun `BannerCarouselComponent holds values`() {
        val movie = MovieItemComponent(id = "1", title = "Movie", imageUrl = "path")
        val component = BannerCarouselComponent(
            title = "Banners",
            items = listOf(movie)
        )

        assertEquals("Banners", component.title)
        assertEquals(listOf(movie), component.items)
    }
}
