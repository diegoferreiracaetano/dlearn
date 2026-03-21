package com.diegoferreiracaetano.dlearn.domain.password

import kotlinx.coroutines.flow.Flow

/**
 * Repositório focado exclusivamente em operações de senha.
 * A verificação de identidade (MFA) agora é tratada pelo ChallengeRepository.
 */
interface PasswordRepository {
    fun changePassword(userId: String, newPassword: String): Flow<ChangePasswordResponse>
}
