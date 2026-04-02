package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.domain.usecases.GetSearchDataUseCase
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.mappers.VideoMapper
import com.diegoferreiracaetano.dlearn.ui.screens.SearchScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SearchOrchestratorTest {

    private lateinit var subject: SearchOrchestrator
    private val getSearchDataUseCase = mockk<GetSearchDataUseCase>(relaxed = true)
    private val getHomeDataUseCase = mockk<GetHomeDataUseCase>(relaxed = true)
    private val videoMapper = mockk<VideoMapper>(relaxed = true)
    private val searchScreenBuilder = mockk<SearchScreenBuilder>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        subject = SearchOrchestrator(getSearchDataUseCase, getHomeDataUseCase, videoMapper, searchScreenBuilder)
    }

    @Test
    fun `given an empty search request when execute is called should return the main search screen`() = runTest {
        val request = AppRequest(path = "search")
        val homeData = mockk<HomeDomainData>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { getHomeDataUseCase.execute(lang, userId = userId) } returns homeData
        every { searchScreenBuilder.buildMain(lang, any()) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a search request with a query when execute is called should return the search results screen`() = runTest {
        val query = "Matrix"
        val request = AppRequest(path = "search", params = mapOf("q" to query))
        val expectedScreen = Screen(components = emptyList())

        coEvery { getSearchDataUseCase.execute(query, lang) } returns emptyList()
        every { searchScreenBuilder.buildContent(query, any(), lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }
}
