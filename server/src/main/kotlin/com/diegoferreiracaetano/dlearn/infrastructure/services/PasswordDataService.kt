package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.error.AppError
import com.diegoferreiracaetano.dlearn.domain.error.AppErrorCode
import com.diegoferreiracaetano.dlearn.domain.error.AppException
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository

class PasswordDataService(
    private val challengeDataService: ChallengeDataService,
    private val userRepository: UserRepository,
) {
    companion object {
        private const val ANONYMOUS_USER = "anonymous"
        private const val PASSWORD_CHANGED_SUCCESS = "Password changed successfully"
    }

    suspend fun changePassword(
        request: ChangePasswordRequest,
        userId: String,
        challengeToken: String?,
    ): ChangePasswordResponse {
        val resolvedUserId = resolveUserId(userId, challengeToken)

        val user =
            userRepository.findById(resolvedUserId)
                ?: userRepository.findByEmail(resolvedUserId)
                ?: throw AppException(AppError(AppErrorCode.USER_NOT_FOUND))

        userRepository.update(user, password = request.newPassword)

        return ChangePasswordResponse(message = PASSWORD_CHANGED_SUCCESS)
    }

    private fun resolveUserId(
        userId: String,
        challengeToken: String?,
    ): String =
        challengeToken?.let { token ->
            validateTokenAndGetUserId(token)
        } ?: if (userId != ANONYMOUS_USER) {
            userId
        } else {
            throw AppException(AppError(AppErrorCode.CHALLENGE_REQUIRED))
        }

    private fun validateTokenAndGetUserId(token: String): String {
        if (!challengeDataService.isTokenValidated(token)) {
            throw AppException(AppError(AppErrorCode.INVALID_TOKEN))
        }
        val userIdFromToken = challengeDataService.getUserIdByToken(token)
        challengeDataService.consumeToken(token)
        return userIdFromToken ?: throw AppException(AppError(AppErrorCode.USER_NOT_FOUND))
    }
}
