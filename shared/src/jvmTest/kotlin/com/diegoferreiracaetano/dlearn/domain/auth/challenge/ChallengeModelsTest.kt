package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs

class ChallengeModelsTest {

    @Test
    fun `Challenge holds type and data`() {
        val data = mapOf("phone" to "***123")
        val challenge = Challenge(ChallengeType.OTP_SMS, data)
        assertEquals(ChallengeType.OTP_SMS, challenge.challengeType)
        assertEquals(data, challenge.data)
    }

    @Test
    fun `ChallengeSession holds transactionId and challenge`() {
        val challenge = Challenge(ChallengeType.BIOMETRIC)
        val session = ChallengeSession("tx123", challenge, 1000L)
        assertEquals("tx123", session.transactionId)
        assertEquals(challenge, session.challenge)
        assertEquals(1000L, session.expiresAt)
    }

    @Test
    fun `ChallengeResult Success holds data`() {
        val data = mapOf("header" to "value")
        val result = ChallengeResult.Success(data)
        assertEquals(data, result.data)
    }

    @Test
    fun `ChallengeResult Failure holds error`() {
        val error = RuntimeException("error")
        val result = ChallengeResult.Failure(error)
        assertEquals(error, result.error)
    }

    @Test
    fun `ChallengeResult Cancelled is a ChallengeResult`() {
        assertIs<ChallengeResult>(ChallengeResult.Cancelled)
    }

    @Test
    fun `ChallengeCancelledException has correct message`() {
        val exception = ChallengeCancelledException()
        assertEquals("O desafio de segurança foi cancelado.", exception.message)
    }
}
