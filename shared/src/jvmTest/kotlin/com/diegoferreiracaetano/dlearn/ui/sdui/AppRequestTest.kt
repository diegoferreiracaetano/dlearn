package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppRequestTest {

    @Test
    fun `AppRequest holds values`() {
        val params = mapOf("id" to "1")
        val metadata = mapOf("source" to "push")
        val request = AppRequest(
            path = "home",
            params = params,
            metadata = metadata
        )

        assertEquals("home", request.path)
        assertEquals(params, request.params)
        assertEquals(metadata, request.metadata)
    }

    @Test
    fun `AppRequest defaults`() {
        val request = AppRequest("home")
        assertNull(request.params)
        assertNull(request.metadata)
    }
}
