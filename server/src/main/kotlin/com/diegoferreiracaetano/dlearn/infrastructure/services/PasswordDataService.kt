package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import java.util.concurrent.ConcurrentHashMap

open class PasswordDataService {
    private val pendingChallenges = ConcurrentHashMap<String, String>()
    private val validatedTokens = ConcurrentHashMap<String, Boolean>()

    open fun changePassword(request: ChangePasswordRequest, challengeToken: String?): ChangePasswordResponse {
        validatedTokens.remove(challengeToken)
        return ChangePasswordResponse(message = "Password changed successfully")
    }

    open fun validateChallenge(challengeToken: String): Boolean {
        return validatedTokens.getOrDefault(challengeToken, false)
    }

    open fun generateChallenge(userId: String): String {
        val token = "${SecurityConstants.CHALLENGE_TOKEN_PREFIX}${System.currentTimeMillis()}"
        pendingChallenges[token] = "123456" // Em produção seria enviado por SMS/Email
        return token
    }

    open fun verifyOtp(otpCode: String, challengeToken: String): String? {
        val expectedCode = pendingChallenges[challengeToken]
        
        // Opção de debug: aceita '000000' ou o código correto
        if (otpCode == "000000" || (expectedCode != null && expectedCode == otpCode)) {
            val validatedToken = "validated-$challengeToken"
            pendingChallenges.remove(challengeToken)
            validatedTokens[validatedToken] = true
            return validatedToken
        }
        return null
    }
}
