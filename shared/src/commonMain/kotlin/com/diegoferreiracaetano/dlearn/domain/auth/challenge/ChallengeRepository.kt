package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import kotlinx.coroutines.flow.Flow

/**
 * Interface única e genérica para resolução de qualquer desafio de segurança.
 * A camada de UI agora só precisa enviar a resposta (answer),
 * pois o contexto do desafio (ID e Tipo) é mantido internamente.
 */
interface ChallengeRepository {
    
    /**
     * Resolve o desafio atual.
     * @param answer A resposta do usuário (ex: o código "123456").
     */
    fun resolveChallenge(answer: String): Flow<ChallengeResult>
    
    /**
     * Solicita o reenvio do desafio atual.
     */
    fun resendChallenge(): Flow<Boolean>
}
