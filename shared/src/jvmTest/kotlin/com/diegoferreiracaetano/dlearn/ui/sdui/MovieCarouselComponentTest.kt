package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals

class MovieCarouselComponentTest {

    @Test
    fun `MovieCarouselComponent holds values`() {
        val movie = MovieItemComponent(id = "1", title = "Movie", imageUrl = "path")
        val component = MovieCarouselComponent(
            title = "Carousel",
            items = listOf(movie)
        )

        assertEquals("Carousel", component.title)
        assertEquals(listOf(movie), component.items)
    }
}
