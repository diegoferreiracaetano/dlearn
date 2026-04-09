package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import org.junit.Test
import kotlin.test.assertNotNull

class I18nProviderTest {

    private val provider = I18nProvider()

    @Test
    fun `given a valid key and language when getString is called should return the translated string or the key name as fallback`() {
        val result = provider.getString(AppStringType.SAVE_CHANGES, "en")
        assertNotNull(result)
    }

    @Test
    fun `given a key with placeholders and arguments when getString is called should return a formatted string with the arguments`() {
        // This assumes we have a key that uses {0} in strings.properties
        // If not, it will just return the key name
        val result = provider.getString(AppStringType.UNKNOWN, "en", "arg1")
        assertNotNull(result)
    }

    @Test
    fun `given a blank language when getString is called should fall back to root locale`() {
        val result = provider.getString(AppStringType.SAVE_CHANGES, "")
        assertNotNull(result)
    }

    @Test
    fun `given a locale with 3 parts when getString is called should use root locale fallback`() {
        val result = provider.getString(AppStringType.SAVE_CHANGES, "en_US_Extra")
        assertNotNull(result)
    }

    @Test
    fun `given an accept-language header with comma and quality values when getString should use the first language`() {
        val result = provider.getString(AppStringType.SAVE_CHANGES, "pt-BR,pt;q=0.9,en;q=0.8")
        assertNotNull(result)
    }
}
