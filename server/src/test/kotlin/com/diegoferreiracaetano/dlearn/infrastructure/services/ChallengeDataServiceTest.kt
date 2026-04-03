package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class ChallengeDataServiceTest {

    private val service = ChallengeDataService()

    @Test
    fun `given a userId when generateChallenge is called should return a valid transactionId starting with challenge token prefix`() {
        val transactionId = service.generateChallenge("user1")
        assertNotNull(transactionId)
        assertTrue(transactionId.startsWith(Constants.CHALLENGE_TOKEN_PREFIX))
    }

    @Test
    fun `given a valid OTP when resolveChallenge is called should return a validated token for the correct user`() {
        val transactionId = service.generateChallenge("user1")
        val answers = mapOf(Constants.OTP_KEY to Constants.DEFAULT_MOCK_OTP)

        val token = service.resolveChallenge(transactionId, ChallengeType.OTP_EMAIL, answers)

        assertNotNull(token)
        assertTrue(service.isTokenValidated(token))
        assertEquals("user1", service.getUserIdByToken(token))
    }

    @Test
    fun `given an incorrect OTP when resolveChallenge is called should return null`() {
        val transactionId = service.generateChallenge("user1")
        val answers = mapOf(Constants.OTP_KEY to "wrong")

        val token = service.resolveChallenge(transactionId, ChallengeType.OTP_EMAIL, answers)

        assertNull(token)
    }

    @Test
    fun `given a validated token when consumeToken is called should remove it from the validated tokens map`() {
        val transactionId = service.generateChallenge("user1")
        val answers = mapOf(Constants.OTP_KEY to Constants.DEFAULT_MOCK_OTP)
        val token = service.resolveChallenge(transactionId, ChallengeType.OTP_EMAIL, answers)!!

        service.consumeToken(token)

        assertFalse(service.isTokenValidated(token))
    }

    @Test
    fun `given a valid transactionId when resendChallenge is called should return true`() {
        val transactionId = service.generateChallenge("user2")

        val result = service.resendChallenge(transactionId)

        assertTrue(result)
    }

    @Test
    fun `given an unknown transactionId when resendChallenge is called should return false`() {
        val result = service.resendChallenge("nonexistent-txn")

        assertFalse(result)
    }

    @Test
    fun `given a wrong challenge type when resolveChallenge is called should return null`() {
        val transactionId = service.generateChallenge("user3", ChallengeType.OTP_EMAIL)
        val answers = mapOf(Constants.OTP_KEY to Constants.DEFAULT_MOCK_OTP)

        val token = service.resolveChallenge(transactionId, ChallengeType.UNKNOWN, answers)

        assertNull(token)
    }

    @Test
    fun `given answers without OTP key when resolveChallenge is called should return null`() {
        val transactionId = service.generateChallenge("user4")

        val token = service.resolveChallenge(transactionId, ChallengeType.OTP_EMAIL, emptyMap())

        assertNull(token)
    }

    @Test
    fun `given an unknown transactionId when resolveChallenge is called should return null`() {
        val answers = mapOf(Constants.OTP_KEY to Constants.DEFAULT_MOCK_OTP)

        val token = service.resolveChallenge("unknown-txn", ChallengeType.OTP_EMAIL, answers)

        assertNull(token)
    }

    @Test
    fun `given a non-existing token when getUserIdByToken is called should return null`() {
        assertNull(service.getUserIdByToken("invalid-token"))
    }
}
