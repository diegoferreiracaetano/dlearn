package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class MovieItemComponentTest {

    @Test
    fun `MovieItemComponent holds values`() {
        val component = MovieItemComponent(
            id = "1",
            title = "Title",
            subtitle = "Subtitle",
            imageUrl = "image",
            rating = "8.5",
            year = "2024",
            duration = "120m",
            contentRating = "14",
            genre = "Action",
            movieType = "Movie",
            isPremium = true,
            isFavorite = true,
            rank = 1,
            actionUrl = "url"
        )
        
        assertEquals("1", component.id)
        assertEquals("Title", component.title)
        assertEquals("Subtitle", component.subtitle)
        assertEquals("image", component.imageUrl)
        assertEquals("8.5", component.rating)
        assertEquals("2024", component.year)
        assertEquals("120m", component.duration)
        assertEquals("14", component.contentRating)
        assertEquals("Action", component.genre)
        assertEquals("Movie", component.movieType)
        assertEquals(true, component.isPremium)
        assertEquals(true, component.isFavorite)
        assertEquals(1, component.rank)
        assertEquals("url", component.actionUrl)
    }

    @Test
    fun `MovieItemComponent defaults`() {
        val component = MovieItemComponent(id = "1", title = "Title", imageUrl = "image")
        assertNull(component.subtitle)
        assertNull(component.rating)
        assertNull(component.year)
        assertNull(component.duration)
        assertNull(component.contentRating)
        assertNull(component.genre)
        assertNull(component.movieType)
        assertFalse(component.isPremium)
        assertFalse(component.isFavorite)
        assertNull(component.rank)
        assertNull(component.actionUrl)
    }
}
