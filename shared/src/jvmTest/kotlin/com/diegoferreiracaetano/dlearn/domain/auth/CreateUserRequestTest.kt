package com.diegoferreiracaetano.dlearn.domain.auth

import kotlin.test.Test
import kotlin.test.assertEquals

class CreateUserRequestTest {

    @Test
    fun `CreateUserRequest holds values`() {
        val request = CreateUserRequest("Name", "Email", "Pass")
        assertEquals("Name", request.name)
        assertEquals("Email", request.email)
        assertEquals("Pass", request.password)
    }
}
