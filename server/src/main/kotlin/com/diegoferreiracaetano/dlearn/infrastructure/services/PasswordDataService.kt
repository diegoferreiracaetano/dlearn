package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse

class PasswordDataService(
    private val challengeDataService: ChallengeDataService
) {
    fun changePassword(request: ChangePasswordRequest, challengeToken: String?): ChangePasswordResponse {
        challengeToken?.let { token ->
            if (!challengeDataService.isTokenValidated(token)) {
                throw SecurityException("Invalid or expired challenge token")
            }
            challengeDataService.consumeToken(token)
        }

        return ChangePasswordResponse(message = "Password changed successfully")
    }
}
