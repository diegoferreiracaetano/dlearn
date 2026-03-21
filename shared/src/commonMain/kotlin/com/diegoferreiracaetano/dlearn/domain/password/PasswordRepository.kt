package com.diegoferreiracaetano.dlearn.domain.password

import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun changePassword(userId: String, newPassword: String): Flow<ChangePasswordResponse>
    fun verifyOtp(request: VerifyOtpRequest): Flow<VerifyOtpResponse>
}
