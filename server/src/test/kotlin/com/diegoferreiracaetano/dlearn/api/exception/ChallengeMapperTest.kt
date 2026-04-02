package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeCode
import com.diegoferreiracaetano.dlearn.domain.models.ChallengeError
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertNotNull

class ChallengeMapperTest {

    private val mapper = ChallengeMapper()

    @Test
    fun `given a ChallengeException when toChallengeSession is called should return a ChallengeSession`() {
        val error = ChallengeError(
            code = ChallengeCode.CHALLENGE_REQUIRED,
            message = "OTP required",
            challengeToken = "txn-123",
        )
        val exception = ChallengeException(error)

        val session = mapper.toChallengeSession(exception)

        assertNotNull(session)
        assertEquals("txn-123", session.transactionId)
    }

    @Test
    fun `given a ChallengeException without preferred type when toChallengeSession is called should default to OTP_EMAIL`() {
        val error = ChallengeError(
            code = ChallengeCode.CHALLENGE_REQUIRED,
            message = "OTP required",
            challengeToken = "txn-123",
        )
        val exception = ChallengeException(error)

        val session = mapper.toChallengeSession(exception)

        assertNotNull(session)
        assertEquals(ChallengeType.OTP_EMAIL, session.challenge.challengeType)
    }

    @Test
    fun `given a ChallengeException with preferred type when toChallengeSession is called should use that type`() {
        val error = ChallengeError(
            code = ChallengeCode.CHALLENGE_REQUIRED,
            message = "OTP required",
            challengeToken = "txn-123",
        )
        val exception = ChallengeException(error)

        val session = mapper.toChallengeSession(exception, preferredType = ChallengeType.UNKNOWN)

        assertNotNull(session)
        assertEquals(ChallengeType.UNKNOWN, session.challenge.challengeType)
    }

    @Test
    fun `given a non-ChallengeException when toChallengeSession is called should return null`() {
        val exception = RuntimeException("some error")

        val session = mapper.toChallengeSession(exception)

        assertNull(session)
    }

    @Test
    fun `given a ChallengeException when toChallengeSession is called should include message in challenge data`() {
        val error = ChallengeError(
            code = ChallengeCode.CHALLENGE_REQUIRED,
            message = "Enter your OTP",
            challengeToken = "txn-abc",
        )
        val exception = ChallengeException(error)

        val session = mapper.toChallengeSession(exception)

        assertNotNull(session)
        assertEquals("Enter your OTP", session.challenge.data["message"])
    }
}
