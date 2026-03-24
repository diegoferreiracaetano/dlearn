package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlinx.coroutines.test.runTest

class ChallengeEngineTest {

    @Test
    fun when_resolve_is_called_with_a_session_it_should_find_and_call_the_correct_handler() = runTest {
        // Given
        val mockHandler = object : ChallengeHandler {
            override fun canHandle(challenge: Challenge): Boolean = challenge.challengeType == ChallengeType.OTP_SMS
            override suspend fun handle(challenge: Challenge, session: ChallengeSession): ChallengeResult {
                return ChallengeResult.Success(mapOf("X-Challenge-Token" to "valid_token"))
            }
        }
        
        val coordinator = ChallengeCoordinator(GlobalEventDispatcher())
        val engine = ChallengeEngine(coordinator, listOf(mockHandler))
        val session = ChallengeSession(
            transactionId = "123",
            challenge = Challenge(ChallengeType.OTP_SMS)
        )

        // When
        val result = engine.resolve(session)

        // Then
        assertTrue(result is ChallengeResult.Success)
        assertEquals("valid_token", result.data["X-Challenge-Token"])
    }

    @Test
    fun when_no_handler_is_found_it_should_return_failure() = runTest {
        // Given
        val coordinator = ChallengeCoordinator(GlobalEventDispatcher())
        val engine = ChallengeEngine(coordinator, emptyList())
        val session = ChallengeSession(
            transactionId = "123",
            challenge = Challenge(ChallengeType.OTP_SMS)
        )

        // When
        val result = engine.resolve(session)

        // Then
        assertTrue(result is ChallengeResult.Failure)
    }
}
