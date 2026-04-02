package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class AppOrchestratorTest {

    private val homeOrchestrator = mockk<Orchestrator>()
    private val orchestrators = mapOf(AppNavigationRoute.HOME to homeOrchestrator)
    private val appOrchestrator = AppOrchestrator(orchestrators)

    @Test
    fun `given a home path when execute is called should delegate the request to the home orchestrator`() {
        val request = AppRequest(path = AppNavigationRoute.HOME)
        val header = AppHeader()
        val expectedScreen = flowOf(mockk<Screen>())
        
        every { homeOrchestrator.execute(request, header, "user1") } returns expectedScreen

        val result = appOrchestrator.execute(request, header, "user1")

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given an invalid path when execute is called should throw an IllegalArgumentException`() {
        val request = AppRequest(path = "invalid")
        val header = AppHeader()

        assertFailsWith<IllegalArgumentException> {
            appOrchestrator.execute(request, header, "user1")
        }
    }
}
