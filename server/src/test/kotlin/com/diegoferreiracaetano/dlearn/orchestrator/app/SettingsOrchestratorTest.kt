package com.diegoferreiracaetano.dlearn.orchestrator.app

import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.ui.screens.SettingsScreenBuilder
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class SettingsOrchestratorTest {

    private lateinit var subject: SettingsOrchestrator
    private val builder = mockk<SettingsScreenBuilder>(relaxed = true)

    private val userId = "user123"
    private val lang = "pt-BR"
    private val header = AppHeader(paramLanguage = lang)

    @Before
    fun setup() {
        subject = SettingsOrchestrator(builder)
    }

    @Test
    fun `given a notifications settings path when execute is called should return the notifications settings screen`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.SETTINGS_NOTIFICATIONS)
        val expectedScreen = Screen(components = emptyList())

        every { builder.buildNotificationScreen(any(), lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a language settings path when execute is called should return the language settings screen`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.SETTINGS_LANGUAGE)
        val expectedScreen = Screen(components = emptyList())

        every { builder.buildLanguageScreen(lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }

    @Test
    fun `given a country settings path when execute is called should return the country settings screen`() = runTest {
        val request = AppRequest(path = AppNavigationRoute.SETTINGS_COUNTRY)
        val expectedScreen = Screen(components = emptyList())

        every { builder.buildCountryScreen(any(), lang) } returns expectedScreen

        val result = subject.execute(request, header, userId).first()

        assertEquals(expectedScreen, result)
    }
}
