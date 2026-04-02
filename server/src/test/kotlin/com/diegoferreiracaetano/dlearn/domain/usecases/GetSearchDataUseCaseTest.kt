package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.infrastructure.services.SearchDataService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetSearchDataUseCaseTest {

    private val searchDataService = mockk<SearchDataService>(relaxed = true)
    private val useCase = GetSearchDataUseCase(searchDataService)

    @Test
    fun `given a query when execute is called should return search results from the service`() = runTest {
        val expectedResults = listOf(mockk<Video>())
        coEvery { searchDataService.search("Matrix", "en") } returns expectedResults

        val result = useCase.execute("Matrix", "en")

        assertEquals(expectedResults, result)
    }
}
