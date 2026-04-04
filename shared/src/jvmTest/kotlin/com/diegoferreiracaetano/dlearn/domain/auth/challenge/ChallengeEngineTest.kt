package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChallengeEngineTest {

    private val coordinator = mockk<ChallengeCoordinator>(relaxed = true)
    private val handler = mockk<ChallengeHandler>()
    private val subject = ChallengeEngine(coordinator, listOf(handler))

    @Test
    fun `given a session when a handler exists should call coordinator emit and handler handle`() = runTest {
        val challenge = Challenge(challengeType = ChallengeType.OTP_SMS, data = emptyMap())
        val session = ChallengeSession(challenge = challenge, transactionId = "sess_123")
        val expectedResult = ChallengeResult.Success(mapOf("token" to "123"))
        
        every { handler.canHandle(challenge) } returns true
        coEvery { handler.handle(challenge, session) } returns expectedResult
        
        val result = subject.resolve(session)
        
        assertEquals(expectedResult, result)
        coVerify { coordinator.emit(session, challenge) }
    }

    @Test
    fun `given a session when no handler exists should return Failure`() = runTest {
        val challenge = Challenge(challengeType = ChallengeType.UNKNOWN, data = emptyMap())
        val session = ChallengeSession(challenge = challenge, transactionId = "sess_123")
        
        every { handler.canHandle(challenge) } returns false
        
        val result = subject.resolve(session)
        
        assertTrue(result is ChallengeResult.Failure)
    }
}
