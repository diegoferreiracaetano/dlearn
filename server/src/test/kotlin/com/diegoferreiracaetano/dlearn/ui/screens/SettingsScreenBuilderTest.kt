package com.diegoferreiracaetano.dlearn.ui.screens

import com.diegoferreiracaetano.dlearn.ui.mappers.SettingsMapper
import com.diegoferreiracaetano.dlearn.ui.sdui.AppContainerComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.Component
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SettingsScreenBuilderTest {

    private val mapper = mockk<SettingsMapper>(relaxed = true)
    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val builder = SettingsScreenBuilder(mapper, i18n)

    @Test
    fun `given notifications enabled when buildNotificationScreen is called should return screen with container`() {
        val rows = listOf(mockk<Component>(), mockk<Component>())
        every { mapper.toNotificationRows(true, any()) } returns rows

        val screen = builder.buildNotificationScreen(notificationsEnabled = true, lang = "en")

        assertEquals(1, screen.components.size)
        val container = screen.components.first() as AppContainerComponent
        assertEquals(2, container.components.size)
    }

    @Test
    fun `given notifications disabled when buildNotificationScreen is called should return screen with container`() {
        val rows = listOf(mockk<Component>())
        every { mapper.toNotificationRows(false, any()) } returns rows

        val screen = builder.buildNotificationScreen(notificationsEnabled = false, lang = "en")

        assertEquals(1, screen.components.size)
        assertTrue(screen.components.first() is AppContainerComponent)
    }

    @Test
    fun `given a language when buildLanguageScreen is called should return screen with container`() {
        val rows = listOf(mockk<Component>(), mockk<Component>(), mockk<Component>())
        every { mapper.toLanguageRows(any()) } returns rows

        val screen = builder.buildLanguageScreen("pt-BR")

        assertEquals(1, screen.components.size)
        val container = screen.components.first() as AppContainerComponent
        assertEquals(3, container.components.size)
    }

    @Test
    fun `given a country when buildCountryScreen is called should return screen with container`() {
        val rows = listOf(mockk<Component>(), mockk<Component>())
        every { mapper.toCountryRows(any(), any()) } returns rows

        val screen = builder.buildCountryScreen(currentCountry = "BR", lang = "pt")

        assertEquals(1, screen.components.size)
        assertTrue(screen.components.first() is AppContainerComponent)
    }
}
