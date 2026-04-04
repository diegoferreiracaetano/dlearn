package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class AppRequestTest {

    @Test
    fun `AppRequest holds path, params and metadata`() {
        val params = mapOf("id" to "123")
        val metadata = mapOf("source" to "push")
        val request = AppRequest("/home", params, metadata)
        assertEquals("/home", request.path)
        assertEquals(params, request.params)
        assertEquals(metadata, request.metadata)
    }

    @Test
    fun `AppRequest defaults are null`() {
        val request = AppRequest("/home")
        assertNull(request.params)
        assertNull(request.metadata)
    }
}
