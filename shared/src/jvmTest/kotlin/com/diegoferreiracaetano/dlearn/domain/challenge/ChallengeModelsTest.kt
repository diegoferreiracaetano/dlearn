package com.diegoferreiracaetano.dlearn.domain.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChallengeModelsTest {

    @Test
    fun `ChallengeStatus values are correct`() {
        assertEquals("SUCCESS", ChallengeStatus.SUCCESS.name)
        assertEquals("CHALLENGE_REQUIRED", ChallengeStatus.CHALLENGE_REQUIRED.name)
        assertEquals("ERROR", ChallengeStatus.ERROR.name)
    }

    @Test
    fun `ChallengeCode values are correct`() {
        assertEquals("CHALLENGE_REQUIRED", ChallengeCode.CHALLENGE_REQUIRED.name)
    }

    @Test
    fun `ChallengeError holds properties`() {
        val error = ChallengeError(ChallengeCode.CHALLENGE_REQUIRED, "msg", "token")
        assertEquals(ChallengeCode.CHALLENGE_REQUIRED, error.code)
        assertEquals("msg", error.message)
        assertEquals("token", error.challengeToken)
    }

    @Test
    fun `ResolveChallengeRequest holds properties`() {
        val answers = mapOf("code" to "123456")
        val request = ResolveChallengeRequest("tx1", ChallengeType.OTP_SMS, answers)
        assertEquals("tx1", request.transactionId)
        assertEquals(ChallengeType.OTP_SMS, request.type)
        assertEquals(answers, request.answers)
    }

    @Test
    fun `ResolveChallengeResponse holds properties`() {
        val response = ResolveChallengeResponse("token", true, "msg")
        assertEquals("token", response.validatedToken)
        assertTrue(response.success)
        assertEquals("msg", response.message)
    }

    @Test
    fun `ChallengeException has correct message`() {
        val error = ChallengeError(ChallengeCode.CHALLENGE_REQUIRED, "Security check", "token")
        val exception = ChallengeException(error)
        assertEquals("Security check", exception.message)
        assertEquals(error, exception.error)
    }
}
