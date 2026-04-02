package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class MovieDetailMapperTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val mapper = MovieDetailMapper(i18n)

    private val data = MovieDetailDomainData(
        id = "1",
        title = "Inception",
        imageUrl = "url",
        year = "2010",
        duration = "2h 28m",
        genre = "Sci-Fi",
        rating = "8.8",
        trailerId = "id",
        isFavorite = true,
        isInWatchlist = false,
        mediaType = MediaType.MOVIES,
        storyLine = "A thief who steals corporate secrets...",
        cast = listOf(mockk(relaxed = true)),
        providers = listOf(mockk(relaxed = true)),
        seasons = emptyList()
    )

    @Test
    fun `given movie detail data when toHeader is called should map it correctly to header component`() {
        every { i18n.getString(AppStringType.DETAIL_WATCH_NOW, "en") } returns "Watch Now"
        
        val result = mapper.toHeader(data, "en")

        assertEquals("Inception", result.title)
        assertEquals(8.8, result.rating)
        assertEquals(true, result.isFavorite)
        assertEquals(1, result.providers.size)
    }

    @Test
    fun `given movie detail data when toStoryLine is called should map it correctly to storyline component`() {
        every { i18n.getString(AppStringType.DETAIL_STORY_LINE, "en") } returns "Storyline"
        
        val result = mapper.toStoryLine(data, "en")

        assertEquals("Storyline", result.title)
        assertEquals(data.storyLine, result.text)
    }

    @Test
    fun `given movie detail data when toCastCarousel is called should map it correctly to cast carousel component`() {
        every { i18n.getString(AppStringType.DETAIL_CAST_CREW, "en") } returns "Cast"
        
        val result = mapper.toCastCarousel(data, "en")

        assertEquals("Cast", result.title)
        assertEquals(1, result.items.size)
    }

    @Test
    fun `given movie detail data with no seasons when toEpisodesSection is called should return null`() {
        val result = mapper.toEpisodesSection(data, "en")
        assertNull(result)
    }
}
