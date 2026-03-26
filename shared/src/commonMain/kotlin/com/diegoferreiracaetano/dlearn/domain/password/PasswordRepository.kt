package com.diegoferreiracaetano.dlearn.domain.password

import kotlinx.coroutines.flow.Flow

interface PasswordRepository {
    fun changePassword(newPassword: String): Flow<ChangePasswordResponse>
}
