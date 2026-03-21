package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.Constants
import java.util.concurrent.ConcurrentHashMap

class ChallengeDataService {
    private val pendingChallenges = ConcurrentHashMap<String, String>()
    private val validatedTokens = ConcurrentHashMap<String, Boolean>()

    fun isTokenValidated(token: String): Boolean = validatedTokens.getOrDefault(token, false)

    fun consumeToken(token: String) {
        validatedTokens.remove(token)
    }

    fun generateChallenge(userId: String): String {
        val transactionId = "${Constants.CHALLENGE_TOKEN_PREFIX}${System.currentTimeMillis()}"
        pendingChallenges[transactionId] = Constants.DEFAULT_MOCK_OTP
        return transactionId
    }

    fun resendChallenge(transactionId: String): Boolean {
        if (!pendingChallenges.containsKey(transactionId)) return false
        
        // Em um cenário real, aqui dispararíamos o e-mail/SMS novamente
        // Para o mock, apenas renovamos o valor no mapa
        pendingChallenges[transactionId] = Constants.DEFAULT_MOCK_OTP
        return true
    }

    fun resolveChallenge(transactionId: String, answers: Map<String, String>): String? {
        val otp = answers[Constants.OTP_KEY] ?: return null
        val expectedCode = pendingChallenges[transactionId]
        
        val isValid = otp == Constants.DEBUG_OTP || (expectedCode != null && expectedCode == otp)

        return if (isValid) {
            val validatedToken = "${Constants.VALIDATED_TOKEN_PREFIX}$transactionId"
            pendingChallenges.remove(transactionId)
            validatedTokens[validatedToken] = true
            validatedToken
        } else null
    }
}
