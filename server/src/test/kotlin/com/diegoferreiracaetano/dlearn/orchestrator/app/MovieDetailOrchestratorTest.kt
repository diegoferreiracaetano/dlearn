package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.usecases.GetMovieDetailUseCase
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.MovieDetailScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class MovieDetailOrchestratorTest {

    private lateinit var subject: MovieDetailOrchestrator
    private val getMovieDetailUseCase = mockk<GetMovieDetailUseCase>(relaxed = true)
    private val screenBuilder = mockk<MovieDetailScreenBuilder>(relaxed = true)
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private val watchlistRepository = mockk<WatchlistRepository>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)
    private val movieId = "550"

    @Before
    fun setup() {
        subject = MovieDetailOrchestrator(getMovieDetailUseCase, screenBuilder, favoriteRepository, watchlistRepository)
    }

    @Test
    fun `given a movie detail request with a valid ID when execute is called should return the movie detail screen`() = runTest {
        val request = AppRequest(path = "movie_detail", params = mapOf(AppQueryParam.ID to movieId))
        val domainData = mockk<MovieDetailDomainData>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { getMovieDetailUseCase.execute(movieId, lang, userId) } returns domainData
        every { screenBuilder.build(domainData, lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a movie detail request without an ID when execute is called should throw an IllegalArgumentException`() = runTest {
        val request = AppRequest(path = "movie_detail")

        assertFailsWith<IllegalArgumentException> {
            subject.execute(request, header, userId).first()
        }
    }

    @Test
    fun `given a request to toggle a favorite in movie detail when execute is called should update the favorite repository and return the screen`() = runTest {
        val request = AppRequest(
            path = "movie_detail",
            params = mapOf(
                AppQueryParam.ID to movieId,
                "favorite" to "true"
            )
        )
        val domainData = mockk<MovieDetailDomainData>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { getMovieDetailUseCase.execute(movieId, lang, userId) } returns domainData
        every { screenBuilder.build(domainData, lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        coVerify { favoriteRepository.toggleFavorite(userId, movieId, true) }
        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a request to toggle a watchlist item in movie detail when execute is called should update the watchlist repository and return the screen`() = runTest {
        val request = AppRequest(
            path = "movie_detail",
            params = mapOf(
                AppQueryParam.ID to movieId,
                "watchlist" to "true"
            )
        )
        val domainData = mockk<MovieDetailDomainData>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { getMovieDetailUseCase.execute(movieId, lang, userId) } returns domainData
        every { screenBuilder.build(domainData, lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        coVerify { watchlistRepository.toggleWatchlist(userId, movieId, true) }
        assertEquals(expectedScreen, result)
    }
}
