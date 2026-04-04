package com.diegoferreiracaetano.dlearn.data.auth.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class BiometricChallengeHandlerTest {

    private val biometricProvider = mockk<BiometricProvider>()
    private val handler = BiometricChallengeHandler(biometricProvider)

    @Test
    fun `when canHandle is called with BIOMETRIC type should return true`() {
        val biometric = Challenge(challengeType = ChallengeType.BIOMETRIC)
        val otp = Challenge(challengeType = ChallengeType.OTP_SMS)

        assertTrue(handler.canHandle(biometric))
        assertFalse(handler.canHandle(otp))
    }

    @Test
    fun `when handle is called and authentication succeeds should return Success`() = runTest {
        val challenge = Challenge(challengeType = ChallengeType.BIOMETRIC)
        val session = ChallengeSession(transactionId = "tx123", challenge = challenge)
        coEvery { biometricProvider.authenticate() } returns BiometricResult.Success("bio_token")

        val result = handler.handle(challenge, session)

        assertTrue(result is ChallengeResult.Success)
        assertEquals("bio_token", result.data["validatedToken"])
    }

    @Test
    fun `when handle is called and authentication fails should return Cancelled`() = runTest {
        val challenge = Challenge(challengeType = ChallengeType.BIOMETRIC)
        val session = ChallengeSession(transactionId = "tx123", challenge = challenge)
        coEvery { biometricProvider.authenticate() } returns BiometricResult.Failure

        val result = handler.handle(challenge, session)

        assertEquals(ChallengeResult.Cancelled, result)
    }

    @Test
    fun `when handle is called and authentication throws should return Failure`() = runTest {
        val challenge = Challenge(challengeType = ChallengeType.BIOMETRIC)
        val session = ChallengeSession(transactionId = "tx123", challenge = challenge)
        val exception = RuntimeException("error")
        coEvery { biometricProvider.authenticate() } throws exception

        val result = handler.handle(challenge, session)

        assertTrue(result is ChallengeResult.Failure)
        assertEquals(exception, result.error)
    }
}
