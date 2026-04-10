package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.domain.models.EpisodeDomainData
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.models.SeasonDomainData
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.ui.sdui.AppEpisodeComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.ui.sdui.ChipGroupComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class MovieDetailMapperTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val mapper = MovieDetailMapper(i18n)

    private val data = MovieDetailDomainData(
        id = "SERIES_1",
        title = "Breaking Bad",
        imageUrl = "url",
        year = "2008",
        duration = "45m",
        genre = "Drama",
        rating = "9.5",
        trailerId = "id",
        isFavorite = true,
        isInWatchlist = false,
        mediaType = MediaType.SERIES,
        storyLine = "Overview",
        cast = listOf(mockk(relaxed = true)),
        providers = listOf(mockk(relaxed = true)),
        seasons = listOf(SeasonDomainData(number = 1, episodeCount = 7)),
        episodes = listOf(
            EpisodeDomainData(
                id = 1,
                name = "Pilot",
                overview = "Overview",
                episodeNumber = 1,
                seasonNumber = 1,
                imageUrl = "ep_url",
                duration = "58"
            )
        )
    )

    @Test
    fun `given movie detail data when toHeader is called should map it correctly to header component`() {
        every { i18n.getString(AppStringType.DETAIL_WATCH_NOW, "en") } returns "Watch Now"

        val result = mapper.toHeader(data, "en")

        assertEquals("Breaking Bad", result.title)
        assertEquals(9.5, result.rating)
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
    fun `given series detail data with seasons and episodes when toEpisodesSection is called should return components`() {
        every { i18n.getString(AppStringType.DETAIL_SEASON, "en") } returns "Season"

        val result = mapper.toEpisodesSection(data, "en", selectedSeason = 1)

        assertEquals(2, result.size)
        assertTrue(result[0] is ChipGroupComponent)
        assertTrue(result[1] is AppEpisodeComponent)

        val chipGroup = result[0] as ChipGroupComponent
        assertEquals("Season 1", chipGroup.items[0].label)

        val episodeComp = result[1] as AppEpisodeComponent
        assertEquals("Pilot", episodeComp.title)
        assertEquals("58m", episodeComp.duration)
    }

    @Test
    fun `given a movie detail domain data when toVideo is called should convert to Video correctly`() {
        val result = data.toVideo()

        assertEquals(data.id, result.id)
        assertEquals(data.title, result.title)
        assertEquals(data.year, result.subtitle)
        assertEquals(data.isFavorite, result.isFavorite)
        assertEquals(data.mediaType, result.mediaType)
    }
}
