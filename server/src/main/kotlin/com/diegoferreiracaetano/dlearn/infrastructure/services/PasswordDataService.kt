package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository

class PasswordDataService(
    private val challengeDataService: ChallengeDataService,
    private val userRepository: UserRepository
) {
    suspend fun changePassword(request: ChangePasswordRequest, userId: String, challengeToken: String?): ChangePasswordResponse {
        val resolvedUserId = challengeToken?.let { token ->
            if (!challengeDataService.isTokenValidated(token)) {
                throw SecurityException("Invalid or expired challenge token")
            }
            val userIdFromToken = challengeDataService.getUserIdByToken(token)
            challengeDataService.consumeToken(token)
            userIdFromToken
        } ?: if (userId != "anonymous") userId else throw SecurityException("Challenge token required for password change")

        if (resolvedUserId == null) throw NoSuchElementException("User not found for the provided token")

        val user = userRepository.findById(resolvedUserId) ?: userRepository.findByEmail(resolvedUserId)
            ?: throw NoSuchElementException("User not found")

        userRepository.update(user, password = request.newPassword)

        return ChangePasswordResponse(message = "Password changed successfully")
    }
}
