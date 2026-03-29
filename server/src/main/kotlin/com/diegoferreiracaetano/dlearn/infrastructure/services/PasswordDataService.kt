package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository

class PasswordDataService(
    private val challengeDataService: ChallengeDataService,
    private val userRepository: UserRepository
) {
    suspend fun changePassword(request: ChangePasswordRequest, challengeToken: String?): ChangePasswordResponse {
        challengeToken?.let { token ->
            if (!challengeDataService.isTokenValidated(token)) {
                throw SecurityException("Invalid or expired challenge token")
            }
            challengeDataService.consumeToken(token)
        } ?: throw SecurityException("Challenge token required for password change")

        val user = userRepository.findById(request.userId) ?: userRepository.findByEmail(request.userId)
            ?: throw NoSuchElementException("User not found")

        userRepository.update(user, password = request.newPassword)

        return ChangePasswordResponse(message = "Password changed successfully")
    }
}
