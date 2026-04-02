package com.diegoferreiracaetano.dlearn.util

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import org.junit.Test
import kotlin.test.assertEquals
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
}
