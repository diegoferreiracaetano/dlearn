package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import java.util.concurrent.ConcurrentHashMap

class ChallengeDataService {
    private data class ChallengeEntry(val code: String, val type: ChallengeType)
    
    private val pendingChallenges = ConcurrentHashMap<String, ChallengeEntry>()
    private val validatedTokens = ConcurrentHashMap<String, Boolean>()

    fun isTokenValidated(token: String): Boolean = validatedTokens.getOrDefault(token, false)

    fun consumeToken(token: String) {
        validatedTokens.remove(token)
    }

    fun generateChallenge(userId: String, type: ChallengeType = ChallengeType.OTP_EMAIL): String {
        val transactionId = "${Constants.CHALLENGE_TOKEN_PREFIX}${System.currentTimeMillis()}"
        pendingChallenges[transactionId] = ChallengeEntry(Constants.DEFAULT_MOCK_OTP, type)
        return transactionId
    }

    fun resendChallenge(transactionId: String): Boolean {
        val entry = pendingChallenges[transactionId] ?: return false
        
        // Em um cenário real, aqui dispararíamos o e-mail/SMS novamente
        // Para o mock, apenas renovamos o valor no mapa mantendo o tipo
        pendingChallenges[transactionId] = entry.copy(code = Constants.DEFAULT_MOCK_OTP)
        return true
    }

    fun resolveChallenge(transactionId: String, type: ChallengeType, answers: Map<String, String>): String? {
        val otp = answers[Constants.OTP_KEY] ?: return null
        val entry = pendingChallenges[transactionId] ?: return null
        
        // Verifica se o tipo do desafio enviado pelo cliente é o mesmo que foi gerado
        if (entry.type != type) return null
        
        val isValid = otp == Constants.DEBUG_OTP || otp == entry.code

        return if (isValid) {
            val validatedToken = "${Constants.VALIDATED_TOKEN_PREFIX}$transactionId"
            pendingChallenges.remove(transactionId)
            validatedTokens[validatedToken] = true
            validatedToken
        } else null
    }
}
