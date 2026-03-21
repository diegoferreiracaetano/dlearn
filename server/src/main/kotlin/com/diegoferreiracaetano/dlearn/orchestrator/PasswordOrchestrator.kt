package com.diegoferreiracaetano.dlearn.orchestrator

import com.diegoferreiracaetano.dlearn.domain.models.*
import com.diegoferreiracaetano.dlearn.domain.usecases.ChangePasswordUseCase
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PasswordOrchestrator(
    private val changePasswordUseCase: ChangePasswordUseCase
) {
    /**
     * Realiza a alteração de senha.
     * O Orchestrator agora é puramente reativo: ele executa o que lhe é pedido.
     * A segurança (MFA) é garantida pela infraestrutura de rotas (Filtros).
     */
    fun changePassword(
        request: ChangePasswordRequest,
        challengeToken: String? = null
    ): Flow<ChangePasswordResponse> = flow {
        // Se a rota exigia MFA, o Filtro já validou o token.
        // Se a rota for pública, o token pode ser null.
        val response = changePasswordUseCase.execute(request, challengeToken)
        emit(response)
    }
}
