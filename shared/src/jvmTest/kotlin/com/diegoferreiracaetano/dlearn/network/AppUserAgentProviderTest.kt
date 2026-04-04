package com.diegoferreiracaetano.dlearn.network

import com.diegoferreiracaetano.dlearn.Platform
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import io.mockk.every
import io.mockk.mockk
import kotlin.test.Test
import kotlin.test.assertEquals

class AppUserAgentProviderTest {

    private val platform: Platform = mockk()
    private val preferencesRepository: PreferencesRepository = mockk()
    private val provider = AppUserAgentProvider(platform, preferencesRepository)

    @Test
    fun `get returns correct AppUserAgent`() {
        every { platform.appVersion } returns "1.0.1"
        every { platform.name } returns "TestDevice"

        val userAgent = provider.get()

        assertEquals("DLearn", userAgent.appName)
        assertEquals("1.0.1", userAgent.appVersion)
        assertEquals("TestDevice", userAgent.deviceName)
    }

    @Test
    fun `getLanguage returns language from repository`() {
        every { preferencesRepository.language } returns "pt-BR"
        assertEquals("pt-BR", provider.getLanguage())
    }

    @Test
    fun `getCountry returns country from repository`() {
        every { preferencesRepository.country } returns "BR"
        assertEquals("BR", provider.getCountry())
    }
}
