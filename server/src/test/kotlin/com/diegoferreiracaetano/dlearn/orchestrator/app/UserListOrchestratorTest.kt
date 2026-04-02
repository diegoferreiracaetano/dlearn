package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.UserListScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals

class UserListOrchestratorTest {

    private val userRepository = mockk<UserRepository>(relaxed = true)
    private val screenBuilder = mockk<UserListScreenBuilder>(relaxed = true)
    private val orchestrator = UserListOrchestrator(userRepository, screenBuilder)

    @Test
    fun `given a request when execute is called should return the user list screen from the builder`() = runTest {
        val request = mockk<AppRequest>()
        val header = AppHeader(paramLanguage = "en")
        val expectedScreen = mockk<Screen>()
        
        coEvery { userRepository.findAll() } returns emptyList()
        every { screenBuilder.build(any(), "en") } returns expectedScreen

        val result = orchestrator.execute(request, header, "user1").first()

        assertEquals(expectedScreen, result)
    }
}
