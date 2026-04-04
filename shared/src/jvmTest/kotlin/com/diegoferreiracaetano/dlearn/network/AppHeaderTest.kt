package com.diegoferreiracaetano.dlearn.network

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class AppHeaderTest {

    @Test
    fun `userAgent is parsed from paramUserAgent`() {
        val appHeader = AppHeader(paramUserAgent = "MyApp/2.0.0 (Samsung S21)")
        assertEquals("MyApp", appHeader.userAgent.appName)
        assertEquals("2.0.0", appHeader.userAgent.appVersion)
        assertEquals("Samsung S21", appHeader.userAgent.deviceName)
    }

    @Test
    fun `language is parsed from paramLanguage`() {
        val appHeader = AppHeader(paramLanguage = "pt-BR,pt;q=0.9,en-US;q=0.8,en;q=0.7")
        assertEquals("pt-BR", appHeader.language)
    }

    @Test
    fun `language defaults to en-US if paramLanguage is null`() {
        val appHeader = AppHeader(paramLanguage = null)
        assertEquals("en-US", appHeader.language)
    }

    @Test
    fun `country is parsed from paramCountry`() {
        val appHeader = AppHeader(paramCountry = "BR")
        assertEquals("BR", appHeader.country)
    }

    @Test
    fun `country is inferred from language if paramCountry is null`() {
        val appHeader = AppHeader(paramLanguage = "es-MX")
        assertEquals("MX", appHeader.country)
    }

    @Test
    fun `country is null if paramCountry is null and language has no country code`() {
        val appHeader = AppHeader(paramLanguage = "en")
        assertEquals(null, appHeader.country)
    }

    @Test
    fun `notificationsEnabled defaults to true`() {
        val appHeader = AppHeader()
        assertTrue(appHeader.notificationsEnabled)
    }

    @Test
    fun `AppHeader works with empty string language`() {
        val appHeader = AppHeader(paramLanguage = "")
        assertEquals("", appHeader.language)
    }
}
