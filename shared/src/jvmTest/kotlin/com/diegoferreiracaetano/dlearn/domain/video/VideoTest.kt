package com.diegoferreiracaetano.dlearn.domain.video

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class VideoTest {

    @Test
    fun `Video holds all properties`() {
        val video = Video(
            id = "1",
            title = "Title",
            subtitle = "Sub",
            description = "Desc",
            url = "url",
            imageUrl = "image",
            isFavorite = true,
            rating = 4.5f,
            progress = 0.5f,
            type = VideoType.BANNER,
            mediaType = MediaType.SERIES
        )
        assertEquals("1", video.id)
        assertEquals("Title", video.title)
        assertEquals(true, video.isFavorite)
        assertEquals(4.5f, video.rating)
        assertEquals(VideoType.BANNER, video.type)
        assertEquals(MediaType.SERIES, video.mediaType)
    }

    @Test
    fun `Video defaults are correct`() {
        val video = Video("1", "T", "S", "D", "U", "I")
        assertFalse(video.isFavorite)
        assertNull(video.rating)
        assertEquals(0f, video.progress)
        assertEquals(VideoType.DEFAULT, video.type)
        assertEquals(MediaType.MOVIES, video.mediaType)
    }
}
