package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import com.diegoferreiracaetano.dlearn.util.event.GlobalEvent
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class ChallengeCoordinatorTest {

    private val globalEventDispatcher: GlobalEventDispatcher = mockk(relaxed = true)
    private lateinit var coordinator: ChallengeCoordinator

    @Before
    fun setup() {
        coordinator = ChallengeCoordinator(globalEventDispatcher)
    }

    @Test
    fun `emit should update current session and active challenge`() = runTest {
        val challenge = Challenge(challengeType = ChallengeType.OTP_EMAIL)
        val session = ChallengeSession(transactionId = "tx123", challenge = challenge)

        coordinator.emit(session, challenge)

        assertEquals(session, coordinator.currentSession)
        assertEquals(challenge, coordinator.activeChallenge)
        coVerify { globalEventDispatcher.emit(any<GlobalEvent.Challenge>()) }
    }

    @Test
    fun `clear should reset session and challenge`() = runTest {
        val challenge = Challenge(challengeType = ChallengeType.OTP_EMAIL)
        val session = ChallengeSession(transactionId = "tx123", challenge = challenge)
        coordinator.emit(session, challenge)

        coordinator.clear()

        assertNull(coordinator.currentSession)
        assertNull(coordinator.activeChallenge)
    }
}
