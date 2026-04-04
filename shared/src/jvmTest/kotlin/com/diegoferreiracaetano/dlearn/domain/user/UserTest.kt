package com.diegoferreiracaetano.dlearn.domain.user

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNull

class UserTest {

    @Test
    fun `User holds all properties`() {
        val user = User("1", "Name", "Email", "Url", true, "123")
        assertEquals("1", user.id)
        assertEquals("Name", user.name)
        assertEquals("Email", user.email)
        assertEquals("Url", user.imageUrl)
        assertEquals(true, user.isPremium)
        assertEquals("123", user.phoneNumber)
    }

    @Test
    fun `User defaults are correct`() {
        val user = User("1", "Name", "Email")
        assertNull(user.imageUrl)
        assertFalse(user.isPremium)
        assertNull(user.phoneNumber)
    }
}
