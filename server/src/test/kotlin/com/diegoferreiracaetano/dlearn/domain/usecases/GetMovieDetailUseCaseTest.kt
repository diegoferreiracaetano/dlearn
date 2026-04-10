package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.MovieDetailDataService
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class GetMovieDetailUseCaseTest {

    private val movieDetailDataService = mockk<MovieDetailDataService>(relaxed = true)
    private val useCase = GetMovieDetailUseCase(movieDetailDataService)

    @Test
    fun `given a movieId when execute is called should return the movie detail from the service`() = runTest {
        val expectedDetail = mockk<MovieDetailDomainData>()
        coEvery { movieDetailDataService.fetchMovieDetail("123", "en", "user1", any()) } returns expectedDetail

        val result = useCase.execute("123", "en", "user1")

        assertEquals(expectedDetail, result)
    }

    @Test
    fun `given a movieId and season when execute is called should return the movie detail with season from the service`() = runTest {
        val expectedDetail = mockk<MovieDetailDomainData>()
        coEvery { movieDetailDataService.fetchMovieDetail("123", "en", "user1", 2) } returns expectedDetail

        val result = useCase.execute("123", "en", "user1", 2)

        assertEquals(expectedDetail, result)
    }
}
