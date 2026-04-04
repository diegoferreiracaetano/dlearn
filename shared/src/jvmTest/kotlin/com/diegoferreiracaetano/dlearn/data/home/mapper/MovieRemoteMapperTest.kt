package com.diegoferreiracaetano.dlearn.data.home.mapper

import com.diegoferreiracaetano.dlearn.data.home.model.MovieRemote
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MovieRemoteMapperTest {

    @Test
    fun `toVideo should map MovieRemote to Video correctly`() {
        val movieRemote = MovieRemote(
            id = 1,
            title = "Test Movie",
            overview = "Test Overview",
            posterPath = "/path.jpg",
            backdropPath = null
        )

        val video = movieRemote.toVideo()

        assertEquals("1", video.id)
        assertEquals("Test Movie", video.title)
        assertEquals("Test Overview", video.description)
        assertTrue(video.imageUrl.contains("/path.jpg"))
    }

    @Test
    fun `toVideo should handle null posterPath`() {
        val movieRemote = MovieRemote(
            id = 1,
            title = "Test Movie",
            overview = "Test Overview",
            posterPath = null,
            backdropPath = null
        )

        val video = movieRemote.toVideo()

        assertEquals("1", video.id)
        assertEquals("", video.imageUrl)
    }
}
