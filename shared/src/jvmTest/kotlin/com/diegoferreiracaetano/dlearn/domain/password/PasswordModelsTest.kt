package com.diegoferreiracaetano.dlearn.domain.password

import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeStatus
import kotlin.test.Test
import kotlin.test.assertEquals

class PasswordModelsTest {

    @Test
    fun `ChangePasswordRequest holds password`() {
        val request = ChangePasswordRequest("newPass")
        assertEquals("newPass", request.newPassword)
    }

    @Test
    fun `ChangePasswordResponse holds message and status`() {
        val response = ChangePasswordResponse("msg", ChallengeStatus.SUCCESS)
        assertEquals("msg", response.message)
        assertEquals(ChallengeStatus.SUCCESS, response.status)
    }
}
