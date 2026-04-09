package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

class HomeMapperTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val mapper = HomeMapper(i18n)

    private val video = Video(
        id = "1",
        title = "Title",
        subtitle = "2024",
        description = "Desc",
        url = "url",
        imageUrl = "img",
        rating = 9.0f,
        mediaType = MediaType.MOVIES
    )

    @Test
    fun `given a video when toBannerMain is called should map it correctly`() {
        val result = mapper.toBannerMain(video)

        assertEquals("1", result.id)
        assertEquals("Title", result.title)
        assertEquals("img", result.imageUrl)
    }

    @Test
    fun `given a list of videos when toCarousel is called should map them to a MovieCarouselComponent`() {
        every { i18n.getString(AppStringType.FILTER_MOVIES, "en") } returns "Movies"

        val result = mapper.toCarousel("Trending", listOf(video), "en", showRank = true)

        assertEquals("Trending", result.title)
        assertEquals(1, result.items.size)
        assertEquals(1, result.items.first().rank)
    }

    @Test
    fun `given a list of videos when toBannerCarousel is called should map them to a BannerCarouselComponent`() {
        val result = mapper.toBannerCarousel("Featured", listOf(video), "en")

        assertEquals("Featured", result.title)
        assertEquals(1, result.items.size)
    }

    @Test
    fun `given a SERIES video when toCarousel is called should use SERIES label`() {
        val seriesVideo = video.copy(mediaType = MediaType.SERIES)
        every { i18n.getString(AppStringType.FILTER_SERIES, "en") } returns "Series"

        val result = mapper.toCarousel("Shows", listOf(seriesVideo), "en", showRank = false)

        assertEquals("Series", result.items.first().movieType)
    }

    @Test
    fun `given a video with null rating when toCarousel is called should return null rating in item`() {
        val videoNoRating = video.copy(rating = null)

        val result = mapper.toCarousel("Top", listOf(videoNoRating), "en")

        assertEquals(null, result.items.first().rating)
    }
}
