package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.domain.video.VideoCategory
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class VideoMapperTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val mapper = VideoMapper(i18n)

    @Test
    fun `given a video when toMovieItem is called should map fields correctly to movie item component`() {
        val video = Video(
            id = "1",
            title = "Movie",
            subtitle = "2024",
            description = "Desc",
            url = "url",
            imageUrl = "img",
            rating = 8.5f,
            mediaType = MediaType.MOVIES,
            categories = listOf(VideoCategory("1", "Action"))
        )
        every { i18n.getString(AppStringType.FILTER_MOVIES, "pt-BR") } returns "Filmes"

        val result = mapper.toMovieItem(video, "pt-BR")

        assertEquals("1", result.id)
        assertEquals("Movie", result.title)
        assertEquals("2024", result.subtitle)
        assertEquals("img", result.imageUrl)
        assertEquals("8.5", result.rating)
        assertEquals("Action", result.genre)
        assertEquals("Filmes", result.movieType)
    }

    @Test
    fun `given a list of videos when toMovieItemComponents is called should return a list of mapped components`() {
        val videos = listOf(
            Video(id = "1", title = "M1", subtitle = "2024", description = "D1", url = "u1", imageUrl = "i1", mediaType = MediaType.MOVIES),
            Video(id = "2", title = "M2", subtitle = "2024", description = "D2", url = "u2", imageUrl = "i2", mediaType = MediaType.MOVIES)
        )

        val result = mapper.toMovieItemComponents(videos, "en")

        assertEquals(2, result.size)
        assertNotNull(result.first())
    }
}
