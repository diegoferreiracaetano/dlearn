package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.Video
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SearchDataServiceTest {

    private val movieClient = mockk<MovieClient>(relaxed = true)
    private val service = SearchDataService(movieClient)

    @Test
    fun `given an empty query when search is called should return an empty list`() = runTest {
        val result = service.search("", "en")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `given a query when search is called should return sorted results from the movie client`() = runTest {
        val v1 = mockk<Video>(relaxed = true) { every { rating } returns 5f }
        val v2 = mockk<Video>(relaxed = true) { every { rating } returns 9f }
        coEvery { movieClient.searchMulti("matrix", "en") } returns listOf(v1, v2)

        val result = service.search("matrix", "en")

        assertEquals(2, result.size)
        assertEquals(v2, result[0])
        assertEquals(v1, result[1])
    }
}
