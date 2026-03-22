package com.diegoferreiracaetano.dlearn.infrastructure.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FaqDataServiceTest {

    private val service = FaqDataService()

    @Test
    fun `should fetch privacy-policy in English correctly`() {
        val result = service.fetchFaqContent("privacy-policy", "en")
        assertNotNull(result)
        assertEquals("Privacy Policy", result.title)
        assert(result.content.contains("<h2>Privacy Policy</h2>"))
    }

    @Test
    fun `should fetch privacy-policy in Portuguese correctly`() {
        val result = service.fetchFaqContent("privacy-policy", "pt-BR")
        assertNotNull(result)
        assertEquals("Política de Privacidade", result.title)
        assert(result.content.contains("<h2>Política de Privacidade</h2>"))
    }

    @Test
    fun `should fallback to English for unsupported language`() {
        val result = service.fetchFaqContent("privacy-policy", "es-ES")
        assertNotNull(result)
        assertEquals("Privacy Policy", result.title)
    }

    @Test
    fun `should return null for non-existing reference`() {
        val result = service.fetchFaqContent("non-existing", "en")
        assertNull(result)
    }
}
