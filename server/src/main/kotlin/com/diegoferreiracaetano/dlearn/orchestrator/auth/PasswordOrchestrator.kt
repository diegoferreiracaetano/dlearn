package com.diegoferreiracaetano.dlearn.orchestrator.auth

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.domain.usecases.ChangePasswordUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PasswordOrchestrator(
    private val changePasswordUseCase: ChangePasswordUseCase
) {
    fun changePassword(
        request: ChangePasswordRequest,
        userId: String,
        challengeToken: String?
    ): Flow<ChangePasswordResponse> = flow {
        val response = changePasswordUseCase.execute(request, userId, challengeToken)
        emit(response)
    }
}
