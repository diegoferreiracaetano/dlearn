package com.diegoferreiracaetano.dlearn.data.auth.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BiometricChallengeHandlerTest {

    private val biometricProvider = mockk<BiometricProvider>()
    private val handler = BiometricChallengeHandler(biometricProvider)

    @Test
    fun `given a BIOMETRIC challenge should return true for canHandle`() {
        val challenge = mockk<Challenge>()
        coEvery { challenge.challengeType } returns ChallengeType.BIOMETRIC
        assertTrue(handler.canHandle(challenge))
    }

    @Test
    fun `given a non-BIOMETRIC challenge should return false for canHandle`() {
        val challenge = mockk<Challenge>()
        coEvery { challenge.challengeType } returns ChallengeType.OTP_SMS
        assertTrue(!handler.canHandle(challenge))
    }

    @Test
    fun `given a successful biometric authentication should return Success`() = runTest {
        val challenge = mockk<Challenge>()
        val token = "valid-token"
        coEvery { biometricProvider.authenticate() } returns BiometricResult.Success(token)

        val result = handler.handle(challenge, mockk())

        assertTrue(result is ChallengeResult.Success)
        assertEquals(token, result.data["validatedToken"])
    }

    @Test
    fun `given a failed biometric authentication should return Cancelled`() = runTest {
        val challenge = mockk<Challenge>()
        coEvery { biometricProvider.authenticate() } returns BiometricResult.Failure

        val result = handler.handle(challenge, mockk())

        assertTrue(result is ChallengeResult.Cancelled)
    }

    @Test
    fun `given an exception during biometric authentication should return Failure`() = runTest {
        val challenge = mockk<Challenge>()
        val exception = RuntimeException("Biometric error")
        coEvery { biometricProvider.authenticate() } throws exception

        val result = handler.handle(challenge, mockk())

        assertTrue(result is ChallengeResult.Failure)
        assertEquals(exception, result.error)
    }
}
