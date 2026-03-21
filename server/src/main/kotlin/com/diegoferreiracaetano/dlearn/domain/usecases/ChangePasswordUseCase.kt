package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordRequest
import com.diegoferreiracaetano.dlearn.domain.models.ChangePasswordResponse
import com.diegoferreiracaetano.dlearn.infrastructure.services.PasswordDataService

class ChangePasswordUseCase(private val passwordDataService: PasswordDataService) {
    fun execute(request: ChangePasswordRequest, challengeToken: String? = null): ChangePasswordResponse {
        return passwordDataService.changePassword(request, challengeToken)
    }
}
