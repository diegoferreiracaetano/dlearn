package com.diegoferreiracaetano.dlearn.navigation

import kotlin.test.Test
import kotlin.test.assertEquals

class AppQueryParamTest {

    @Test
    fun `AppQueryParam values are correct`() {
        assertEquals("type", AppQueryParam.TYPE)
        assertEquals("q", AppQueryParam.Q)
        assertEquals("id", AppQueryParam.ID)
        assertEquals("userId", AppQueryParam.USER_ID)
    }
}
