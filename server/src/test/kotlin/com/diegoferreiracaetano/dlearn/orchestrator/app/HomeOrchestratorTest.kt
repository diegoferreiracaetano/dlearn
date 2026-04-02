package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.data.cache.CacheManager
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.domain.usecases.GetHomeDataUseCase
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.HomeScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.*
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import kotlin.test.assertEquals

class HomeOrchestratorTest : KoinTest {

    private lateinit var subject: HomeOrchestrator
    private val getHomeDataUseCase = mockk<GetHomeDataUseCase>(relaxed = true)
    private val screenBuilder = mockk<HomeScreenBuilder>(relaxed = true)
    private val cacheManager = mockk<CacheManager>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        stopKoin()
        startKoin {
            modules(module {
                single { cacheManager }
            })
        }
        every { cacheManager.get<Screen>(any(), any()) } returns null
        subject = HomeOrchestrator(getHomeDataUseCase, screenBuilder)
    }

    @After
    fun tearDown() {
        stopKoin()
    }

    @Test
    fun `given a home request without filters when execute is called should return the home screen`() = runTest {
        val request = AppRequest(path = "home")
        val homeData = mockk<HomeDomainData>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { getHomeDataUseCase.execute(lang, HomeFilterType.ALL, userId) } returns homeData
        every { screenBuilder.build(homeData, lang, HomeFilterType.ALL) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a home request with a specific filter type when execute is called should return the filtered home screen`() = runTest {
        val type = HomeFilterType.MOVIES
        val request = AppRequest(path = "home", params = mapOf("type" to type.name))
        val homeData = mockk<HomeDomainData>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { getHomeDataUseCase.execute(lang, type, userId) } returns homeData
        every { screenBuilder.build(homeData, lang, type) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }
}
