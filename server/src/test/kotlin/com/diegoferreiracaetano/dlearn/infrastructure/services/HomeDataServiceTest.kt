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
}
