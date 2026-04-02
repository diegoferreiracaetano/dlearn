package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import java.util.concurrent.ConcurrentHashMap

class ChallengeDataService {
    private data class ChallengeEntry(
        val userId: String,
        val code: String,
        val type: ChallengeType,
    )

    private val pendingChallenges = ConcurrentHashMap<String, ChallengeEntry>()
    private val validatedTokens = ConcurrentHashMap<String, String>()

    fun isTokenValidated(token: String): Boolean = validatedTokens.containsKey(token)

    fun getUserIdByToken(token: String): String? = validatedTokens[token]

    fun consumeToken(token: String) {
        validatedTokens.remove(token)
    }

    fun generateChallenge(
        userId: String,
        type: ChallengeType = ChallengeType.OTP_EMAIL,
    ): String {
        val transactionId = "${Constants.CHALLENGE_TOKEN_PREFIX}${System.currentTimeMillis()}"
        pendingChallenges[transactionId] = ChallengeEntry(userId, Constants.DEFAULT_MOCK_OTP, type)
        return transactionId
    }

    fun resendChallenge(transactionId: String): Boolean {
        val entry = pendingChallenges[transactionId] ?: return false

        pendingChallenges[transactionId] = entry.copy(code = Constants.DEFAULT_MOCK_OTP)
        return true
    }

    fun resolveChallenge(
        transactionId: String,
        type: ChallengeType,
        answers: Map<String, String>,
    ): String? {
        val otp = answers[Constants.OTP_KEY] ?: return null
        val entry = pendingChallenges[transactionId] ?: return null

        if (entry.type != type) return null

        val isValid = otp == Constants.DEBUG_OTP || otp == entry.code

        return if (isValid) {
            val validatedToken = "${Constants.VALIDATED_TOKEN_PREFIX}$transactionId"
            pendingChallenges.remove(transactionId)
            validatedTokens[validatedToken] = entry.userId
            validatedToken
        } else {
            null
        }
    }
}
