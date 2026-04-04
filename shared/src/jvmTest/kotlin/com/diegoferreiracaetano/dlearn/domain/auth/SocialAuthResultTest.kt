package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class SocialAuthResultTest {

    @Test
    fun `Success can be created and holds values`() {
        val result = SocialAuthResult.Success("id_token", "access_token")
        assertEquals("id_token", result.idToken)
        assertEquals("access_token", result.accessToken)
    }

    @Test
    fun `Failure can be created and holds error`() {
        val result = SocialAuthResult.Failure(AppErrorCode.UNKNOWN_ERROR)
        assertEquals(AppErrorCode.UNKNOWN_ERROR, result.error)
    }

    @Test
    fun `Cancelled is a SocialAuthResult`() {
        assertIs<SocialAuthResult>(SocialAuthResult.Cancelled)
    }
}
