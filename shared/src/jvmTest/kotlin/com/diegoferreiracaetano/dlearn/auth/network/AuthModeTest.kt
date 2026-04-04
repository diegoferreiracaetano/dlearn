package com.diegoferreiracaetano.dlearn.auth.network

import io.ktor.client.request.HttpRequestBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class AuthModeTest {

    @Test
    fun `auth sets the correct attribute`() {
        val builder = HttpRequestBuilder()
        builder.auth(AuthMode.REQUIRED)

        assertEquals(AuthMode.REQUIRED, builder.attributes[AuthModeKey])
    }

    @Test
    fun `auth sets OPTIONAL attribute`() {
        val builder = HttpRequestBuilder()
        builder.auth(AuthMode.OPTIONAL)

        assertEquals(AuthMode.OPTIONAL, builder.attributes[AuthModeKey])
    }

    @Test
    fun `auth sets NONE attribute`() {
        val builder = HttpRequestBuilder()
        builder.auth(AuthMode.NONE)

        assertEquals(AuthMode.NONE, builder.attributes[AuthModeKey])
    }
}
