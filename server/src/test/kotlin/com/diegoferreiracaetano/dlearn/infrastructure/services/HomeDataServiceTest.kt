package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class HomeDataServiceTest {

    private val movieClient = mockk<MovieClient>(relaxed = true)
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private val service = HomeDataService(movieClient, favoriteRepository)

    private val video = Video(
        id = "MOVIES_1",
        title = "T",
        subtitle = "S",
        description = "D",
        url = "U",
        imageUrl = "I",
        mediaType = MediaType.MOVIES
    )

    @Test
    fun `given valid parameters when fetchHomeData is called should return aggregated home data including banner with favorite status`() = runTest {
        coEvery { favoriteRepository.getFavorites("user1") } returns listOf("MOVIES_1")
        coEvery { movieClient.getPopularMovies("en") } returns listOf(video)
        coEvery { movieClient.getTopRatedMovies("en") } returns listOf(video)

        val result = service.fetchHomeData("en", HomeFilterType.MOVIES, "user1")

        assertNotNull(result.banner)
        assertEquals("MOVIES_1", result.banner?.id)
        assertTrue(result.banner?.isFavorite == true)
    }

    @Test
    fun `given SERIES filter type when fetchHomeData is called should return data with series and skip movies`() = runTest {
        val seriesVideo = video.copy(id = "SERIES_1", mediaType = MediaType.SERIES)
        coEvery { movieClient.getPopularSeries("en") } returns listOf(seriesVideo)
        coEvery { movieClient.getTopRatedSeries("en") } returns listOf(seriesVideo)

        val result = service.fetchHomeData("en", HomeFilterType.SERIES, "user2")

        assertNotNull(result)
        assertEquals("SERIES_1", result.banner?.id)
    }

    @Test
    fun `given ALL filter type when fetchHomeData is called should return combined movies and series data`() = runTest {
        coEvery { movieClient.getPopularMovies("en") } returns listOf(video)
        coEvery { movieClient.getPopularSeries("en") } returns listOf(video.copy(id = "SERIES_2"))

        val result = service.fetchHomeData("en", HomeFilterType.ALL, "user3")

        assertNotNull(result)
    }

    @Test
    fun `given favorites repository throws when fetchHomeData is called should handle error gracefully and return empty favorites`() = runTest {
        coEvery { favoriteRepository.getFavorites(any()) } throws RuntimeException("DB error")
        coEvery { movieClient.getPopularMovies("en") } returns listOf(video)

        val result = service.fetchHomeData("en", HomeFilterType.MOVIES, "user4")

        assertNotNull(result)
        assertEquals(false, result.banner?.isFavorite)
    }
}
