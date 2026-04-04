package com.diegoferreiracaetano.dlearn.auth.network

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.ktor.client.request.HttpRequestBuilder
import kotlin.test.Test
import kotlin.test.assertEquals

class ChallengeExtensionsTest {

    @Test
    fun `challengePreference sets the correct header`() {
        val builder = HttpRequestBuilder()
        builder.challengePreference(ChallengeType.OTP_SMS)

        assertEquals(
            ChallengeType.OTP_SMS.name,
            builder.headers[SecurityConstants.HEADER_CHALLENGE_PREFERENCE]
        )
    }

    @Test
    fun `challengePreference with BIOMETRIC sets the correct header`() {
        val builder = HttpRequestBuilder()
        builder.challengePreference(ChallengeType.BIOMETRIC)

        assertEquals(
            ChallengeType.BIOMETRIC.name,
            builder.headers[SecurityConstants.HEADER_CHALLENGE_PREFERENCE]
        )
    }
}
