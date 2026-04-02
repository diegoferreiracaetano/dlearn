package com.diegoferreiracaetano.dlearn.infrastructure.mappers

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.model.TmdbMovieDetailRemote
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals

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
    fun `given a TMDB response with a name property when toMovieDetail is called should map it to a series media type`() {
        val response = TmdbMovieDetailRemote(
            id = 2,
            name = "Breaking Bad",
            posterPath = "/path.jpg",
            firstAirDate = "2008-01-20",
            voteAverage = 9.5,
            overview = "Overview",
            genres = emptyList()
        )
        
        val result = mapper.toMovieDetail(response)

        assertEquals("SERIES_2", result.id)
        assertEquals("Breaking Bad", result.title)
        assertEquals(MediaType.SERIES, result.mediaType)
    }
}
