package com.diegoferreiracaetano.dlearn.ui.mappers

import com.diegoferreiracaetano.dlearn.LocaleConstants
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSelectionRowComponent
import com.diegoferreiracaetano.dlearn.ui.sdui.AppSwitchRowComponent
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class SettingsMapperTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val mapper = SettingsMapper(i18n)

    @Test
    fun `given notifications are enabled when toNotificationRows is called should return a switch component that is checked`() {
        val result = mapper.toNotificationRows(true, "en")
        val switch = result.first() as AppSwitchRowComponent
        assertTrue(switch.isChecked)
    }

    @Test
    fun `given a current language when toLanguageRows is called should return components with the correct language selected`() {
        val result = mapper.toLanguageRows(LocaleConstants.LANG_PT_BR)
        val selected = result.map { it as AppSelectionRowComponent }.find { it.isSelected }
        assertEquals(LocaleConstants.LANG_PT_BR, selected?.value)
        assertEquals(3, result.size)
    }

    @Test
    fun `given a current country when toCountryRows is called should return components with the correct country selected`() {
        val result = mapper.toCountryRows(LocaleConstants.COUNTRY_BR, "pt")
        val selected = result.map { it as AppSelectionRowComponent }.find { it.isSelected }
        assertEquals(LocaleConstants.COUNTRY_BR, selected?.value)
        assertEquals(3, result.size)
    }
}
