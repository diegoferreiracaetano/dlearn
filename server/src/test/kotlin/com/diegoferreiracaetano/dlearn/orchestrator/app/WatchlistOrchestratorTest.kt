package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.WatchlistScreenBuilder
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

class WatchlistOrchestratorTest {

    private lateinit var subject: WatchlistOrchestrator
    private val watchlistScreenBuilder = mockk<WatchlistScreenBuilder>(relaxed = true)
    private val watchlistRepository = mockk<WatchlistRepository>(relaxed = true)
    private val videoMapper = mockk<VideoMapper>(relaxed = true)
    private val movieClient = mockk<MovieClient>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        subject = WatchlistOrchestrator(watchlistScreenBuilder, watchlistRepository, videoMapper, movieClient)
    }

    @Test
    fun `given a request for the watchlist list when execute is called should return the watchlist screen`() = runTest {
        val request = AppRequest(path = "watchlist")
        val expectedScreen = Screen(components = emptyList())

        coEvery { watchlistRepository.getWatchlist(userId) } returns listOf("1", "2")
        every { watchlistScreenBuilder.build(lang, any()) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a request to toggle a watchlist item when execute is called should update the repository and return the screen`() = runTest {
        val movieId = "123"
        val request = AppRequest(
            path = "watchlist",
            params = mapOf(
                AppQueryParam.ID to movieId,
                "watchlist" to "true"
            )
        )
        val expectedScreen = Screen(components = emptyList())

        every { watchlistScreenBuilder.build(lang, any()) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        coVerify { watchlistRepository.toggleWatchlist(userId, movieId, true) }
        assertEquals(expectedScreen, result)
    }
}
