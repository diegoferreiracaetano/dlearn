package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.MainScreenBuilder
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

class MainOrchestratorTest {

    private lateinit var subject: MainOrchestrator
    private val mainScreenBuilder = mockk<MainScreenBuilder>(relaxed = true)
    private val userRepository = mockk<UserRepository>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        subject = MainOrchestrator(mainScreenBuilder, userRepository)
    }

    @Test
    fun `given a request for the main screen when execute is called should return the main screen from the builder`() = runTest {
        val request = AppRequest(path = "main")
        val user = mockk<User>(relaxed = true)
        val expectedScreen = Screen(components = emptyList())

        coEvery { userRepository.findById(userId) } returns user
        every { mainScreenBuilder.build(lang, user) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }
}
