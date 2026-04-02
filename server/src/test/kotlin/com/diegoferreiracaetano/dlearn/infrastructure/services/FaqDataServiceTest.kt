package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.ui.sdui.AppStringType
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FaqDataServiceTest {

    private val i18n = mockk<I18nProvider>(relaxed = true)
    private val service = FaqDataService(i18n)

    @Test
    fun `given a valid reference when fetchFaqContent is called should return the expected faq content`() {
        every { i18n.getString(AppStringType.LEGAL_PRIVACY_TITLE, "en") } returns "Privacy"
        
        val result = service.fetchFaqContent("privacy-policy", "en")

        assertNotNull(result)
        assertEquals("Privacy", result.title)
    }

    @Test
    fun `given an invalid reference when fetchFaqContent is called should return null`() {
        val result = service.fetchFaqContent("unknown", "en")
        assertNull(result)
    }
}
