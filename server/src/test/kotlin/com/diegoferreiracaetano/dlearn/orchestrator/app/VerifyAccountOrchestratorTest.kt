package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.VerifyAccountScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class VerifyAccountOrchestratorTest {

    private val verifyAccountScreenBuilder = mockk<VerifyAccountScreenBuilder>(relaxed = true)
    private val orchestrator = VerifyAccountOrchestrator(verifyAccountScreenBuilder)

    @Test
    fun `given a request for account verification when execute is called should return the verification screen from the builder`() = runTest {
        val request = mockk<AppRequest>()
        val header = AppHeader(paramLanguage = "en")
        val expectedScreen = mockk<Screen>()

        every { verifyAccountScreenBuilder.build("en") } returns expectedScreen

        val result = orchestrator.execute(request, header, "user1").first()

        assertEquals(expectedScreen, result)
    }
}
