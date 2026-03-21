package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class ChallengeEngineTest {

    @Test
    fun `when resolve is called with a session, it should find and call the correct handler`() = runTest {
        // Given
        val mockHandler = object : ChallengeHandler {
            override fun canHandle(challenge: Challenge): Boolean = challenge.challengeType == ChallengeType.OTP_SMS
            override suspend fun handle(challenge: Challenge, session: ChallengeSession): ChallengeResult {
                return ChallengeResult.Success(mapOf("X-Challenge-Token" to "valid_token"))
            }
        }
        
        val engine = ChallengeEngine(listOf(mockHandler))
        val session = ChallengeSession(
            transactionId = "123",
            challenges = listOf(Challenge(ChallengeType.OTP_SMS))
        )

        // When
        val result = engine.resolve(session)

        // Then
        assertTrue(result is ChallengeResult.Success)
        assertEquals("valid_token", result.data["X-Challenge-Token"])
    }

    @Test
    fun `when no handler is found, it should return failure`() = runTest {
        // Given
        val engine = ChallengeEngine(emptyList())
        val session = ChallengeSession(
            transactionId = "123",
            challenges = listOf(Challenge(ChallengeType.OTP_SMS))
        )

        // When
        val result = engine.resolve(session)

        // Then
        assertTrue(result is ChallengeResult.Failure)
    }

    @Test
    fun `when session has no challenges, it should return cancelled`() = runTest {
        // Given
        val engine = ChallengeEngine(emptyList())
        val session = ChallengeSession(
            transactionId = "123",
            challenges = emptyList()
        )

        // When
        val result = engine.resolve(session)

        // Then
        assertEquals(ChallengeResult.Cancelled, result)
    }
}
