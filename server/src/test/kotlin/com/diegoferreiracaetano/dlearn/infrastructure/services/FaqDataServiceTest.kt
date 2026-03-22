package com.diegoferreiracaetano.dlearn.infrastructure.services

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class FaqDataServiceTest {

    private val service = FaqDataService()

    @Test
    fun `should fetch privacy-policy correctly`() {
        val result = service.fetchFaqContent("privacy-policy")
        assertNotNull(result)
        assertEquals("Privacy Policy", result.title)
        assert(result.content.contains("<h1>Privacy Policy</h1>"))
    }

    @Test
    fun `should fetch terms-of-service correctly`() {
        val result = service.fetchFaqContent("terms-of-service")
        assertNotNull(result)
        assertEquals("Terms of Service", result.title)
    }

    @Test
    fun `should return null for non-existing reference`() {
        val result = service.fetchFaqContent("non-existing")
        assertNull(result)
    }
}
