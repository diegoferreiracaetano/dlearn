package com.diegoferreiracaetano.dlearn.infrastructure.mappers

import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.TmdbCastRemote
import com.diegoferreiracaetano.dlearn.model.TmdbCreditsRemote
import com.diegoferreiracaetano.dlearn.model.TmdbEpisodeRemote
import com.diegoferreiracaetano.dlearn.model.TmdbMovieDetailRemote
import com.diegoferreiracaetano.dlearn.model.TmdbSeasonRemote
import com.diegoferreiracaetano.dlearn.model.TmdbVideoRemote
import com.diegoferreiracaetano.dlearn.model.TmdbVideosResponseRemote
import com.diegoferreiracaetano.dlearn.model.TmdbWatchProvidersResponse
import com.diegoferreiracaetano.dlearn.model.WatchProviderRemote
import com.diegoferreiracaetano.dlearn.model.WatchProvidersCountry
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class TmdbMapperTest {

    private val urlMapper = mockk<WatchProviderUrlMapper>(relaxed = true)
    private val mapper = TmdbMapper(urlMapper)

    @Test
    fun `given a TMDB response when toMovieDetail is called should map the fields correctly to a domain movie detail object`() {
        val response = TmdbMovieDetailRemote(
            id = 1,
            title = "Inception",
            posterPath = "/path.jpg",
            releaseDate = "2010-07-16",
            voteAverage = 8.8,
            overview = "Overview",
            genres = emptyList()
        )

        val result = mapper.toMovieDetail(response)

        assertEquals("MOVIES_1", result.id)
        assertEquals("Inception", result.title)
        assertEquals("2010", result.year)
        assertEquals(MediaType.MOVIES, result.mediaType)
    }

    @Test
    fun `given a TMDB response with seasons when toMovieDetail should filter out season 0`() {
        val season0 = TmdbSeasonRemote(id = 0, seasonNumber = 0, episodeCount = 10, name = "Specials")
        val season1 = TmdbSeasonRemote(id = 1, seasonNumber = 1, episodeCount = 20, name = "Season 1")
        val response = TmdbMovieDetailRemote(
            id = 2,
            name = "Breaking Bad",
            genres = emptyList(),
            seasons = listOf(season0, season1)
        )

        val result = mapper.toMovieDetail(response)

        assertEquals(1, result.seasons.size)
        assertEquals(1, result.seasons[0].number)
    }

    @Test
    fun `given episodes when toEpisode should map fields correctly`() {
        val remoteEpisode = TmdbEpisodeRemote(
            id = 10,
            episodeNumber = 1,
            name = "Pilot",
            overview = "First episode",
            seasonNumber = 1,
            stillPath = "/still.jpg",
            runtime = 45
        )

        val episode = mapper.toEpisode(remoteEpisode)

        assertEquals(10, episode.id)
        assertEquals("Pilot", episode.name)
        assertEquals("First episode", episode.overview)
        assertEquals("45", episode.duration)
        assertNotNull(episode.imageUrl)
    }

    @Test
    fun `given a TMDB response with cast when toMovieDetail should map cast members with image url`() {
        val cast = TmdbCastRemote(id = 1, name = "Actor", character = "Role", profilePath = "/actor.jpg")
        val response = TmdbMovieDetailRemote(
            id = 3,
            title = "Movie",
            genres = emptyList(),
            credits = TmdbCreditsRemote(cast = listOf(cast))
        )

        val result = mapper.toMovieDetail(response)

        assertEquals(1, result.cast.size)
        assertEquals("Actor", result.cast[0].name)
        assertEquals("Role", result.cast[0].role)
        assertNotNull(result.cast[0].imageUrl)
    }

    @Test
    fun `given a TMDB response with cast and null profilePath when toMovieDetail should map cast member with null imageUrl`() {
        val cast = TmdbCastRemote(id = 2, name = "Actor2", character = "Role2", profilePath = null)
        val response = TmdbMovieDetailRemote(
            id = 4,
            title = "Movie",
            genres = emptyList(),
            credits = TmdbCreditsRemote(cast = listOf(cast))
        )

        val result = mapper.toMovieDetail(response)

        assertEquals(1, result.cast.size)
        assertNull(result.cast[0].imageUrl)
    }

    @Test
    fun `given a TMDB response with a YouTube trailer when toMovieDetail should extract trailer id`() {
        val video =
            TmdbVideoRemote(
                id = "v1",
                key = "trailer-key",
                name = "Trailer",
                site = TmdbConstants.SITE_YOUTUBE,
                type = TmdbConstants.TYPE_TRAILER
            )
        val response = TmdbMovieDetailRemote(
            id = 5,
            title = "Movie",
            genres = emptyList(),
            videos = TmdbVideosResponseRemote(results = listOf(video))
        )

        val result = mapper.toMovieDetail(response)

        assertEquals("trailer-key", result.trailerId)
    }

    @Test
    fun `given a TMDB response with a YouTube teaser when toMovieDetail should extract teaser id`() {
        val video =
            TmdbVideoRemote(
                id = "v2",
                key = "teaser-key",
                name = "Teaser",
                site = TmdbConstants.SITE_YOUTUBE,
                type = TmdbConstants.TYPE_TEASER
            )
        val response = TmdbMovieDetailRemote(
            id = 6,
            title = "Movie",
            genres = emptyList(),
            videos = TmdbVideosResponseRemote(results = listOf(video))
        )

        val result = mapper.toMovieDetail(response)

        assertEquals("teaser-key", result.trailerId)
    }

    @Test
    fun `given a TMDB response with a non-YouTube video when toMovieDetail should return null trailerId`() {
        val video =
            TmdbVideoRemote(
                id = "v3",
                key = "vimeo-key",
                name = "Clip",
                site = "Vimeo",
                type = TmdbConstants.TYPE_TRAILER
            )
        val response = TmdbMovieDetailRemote(
            id = 7,
            title = "Movie",
            genres = emptyList(),
            videos = TmdbVideosResponseRemote(results = listOf(video))
        )

        val result = mapper.toMovieDetail(response)

        assertNull(result.trailerId)
    }

    @Test
    fun `given a TMDB response with watch providers when toMovieDetail should map providers`() {
        val provider = WatchProviderRemote(providerId = 8, providerName = "Netflix", logoPath = "/logo.jpg")
        val countryData = WatchProvidersCountry(link = "http://link", flatrate = listOf(provider))
        val response = TmdbMovieDetailRemote(
            id = 8,
            title = "Movie",
            genres = emptyList(),
            watchProviders = TmdbWatchProvidersResponse(results = mapOf(TmdbConstants.DEFAULT_REGION to countryData))
        )
        every { urlMapper.buildUrls(any(), any(), any(), any()) } returns mockk(relaxed = true)

        val result = mapper.toMovieDetail(response)

        assertEquals(1, result.providers.size)
        assertEquals("Netflix", result.providers[0].name)
    }

    @Test
    fun `given a TMDB response with isFavorite and isInWatchlist flags when toMovieDetail should pass them through`() {
        val response = TmdbMovieDetailRemote(id = 9, title = "Movie", genres = emptyList())

        val result = mapper.toMovieDetail(response, isFavorite = true, isInWatchlist = true)

        assertEquals(true, result.isFavorite)
        assertEquals(true, result.isInWatchlist)
    }
}
