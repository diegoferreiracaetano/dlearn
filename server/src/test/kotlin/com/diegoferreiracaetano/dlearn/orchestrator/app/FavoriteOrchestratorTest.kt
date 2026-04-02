package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.FavoriteScreenBuilder
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

class FavoriteOrchestratorTest {

    private lateinit var subject: FavoriteOrchestrator
    private val favoriteScreenBuilder = mockk<FavoriteScreenBuilder>(relaxed = true)
    private val favoriteRepository = mockk<FavoriteRepository>(relaxed = true)
    private val videoMapper = mockk<VideoMapper>(relaxed = true)
    private val movieClient = mockk<MovieClient>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        subject = FavoriteOrchestrator(favoriteScreenBuilder, favoriteRepository, videoMapper, movieClient)
    }

    @Test
    fun `given a request for the favorites list when execute is called should return the favorites screen`() = runTest {
        val request = AppRequest(path = "favorite")
        val expectedScreen = Screen(components = emptyList())

        coEvery { favoriteRepository.getFavorites(userId) } returns listOf("1", "2")
        every { favoriteScreenBuilder.build(lang, any()) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a request to toggle a favorite when execute is called should update the repository and return the screen`() = runTest {
        val movieId = "123"
        val request = AppRequest(
            path = "favorite",
            params = mapOf(
                AppQueryParam.ID to movieId,
                "favorite" to "true"
            )
        )
        val expectedScreen = Screen(components = emptyList())

        every { favoriteScreenBuilder.build(lang, any()) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        coVerify { favoriteRepository.toggleFavorite(userId, movieId, true) }
        assertEquals(expectedScreen, result)
    }
}
