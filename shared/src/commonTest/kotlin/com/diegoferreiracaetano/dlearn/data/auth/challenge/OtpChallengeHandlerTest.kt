package com.diegoferreiracaetano.dlearn.data.auth.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import kotlinx.coroutines.async
import kotlinx.coroutines.yield
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class OtpChallengeHandlerTest {

    private val handler = OtpChallengeHandler()

    private val otpSmsChallenge = Challenge(ChallengeType.OTP_SMS)
    private val otpEmailChallenge = Challenge(ChallengeType.OTP_EMAIL)
    private val biometricChallenge = Challenge(ChallengeType.BIOMETRIC)
    private val session = ChallengeSession(transactionId = "tx-1", challenge = otpSmsChallenge)

    @Test
    fun `given OTP_SMS challenge when canHandle is called should return true`() {
        assertTrue(handler.canHandle(otpSmsChallenge))
    }

    @Test
    fun `given OTP_EMAIL challenge when canHandle is called should return true`() {
        assertTrue(handler.canHandle(otpEmailChallenge))
    }

    @Test
    fun `given BIOMETRIC challenge when canHandle is called should return false`() {
        assertFalse(handler.canHandle(biometricChallenge))
    }

    @Test
    fun `given a pending handle when onChallengeResolved is called should complete with success`() = runTest {
        val deferred = async { handler.handle(otpSmsChallenge, session) }
        yield()
        handler.onChallengeResolved("validated-token")

        val result = deferred.await()
        assertTrue(result is ChallengeResult.Success)
        assertEquals("validated-token", result.data["validatedToken"])
    }

    @Test
    fun `given a pending handle when onChallengeCancelled is called should complete with cancelled`() = runTest {
        val deferred = async { handler.handle(otpSmsChallenge, session) }
        yield()
        handler.onChallengeCancelled()

        val result = deferred.await()
        assertTrue(result is ChallengeResult.Cancelled)
    }
}
