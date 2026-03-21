package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import kotlinx.coroutines.flow.Flow

/**
 * Interface única e genérica para resolução de qualquer desafio de segurança.
 */
interface ChallengeRepository {
    
    /**
     * Resolve um desafio de forma agnóstica.
     * 
     * @param transactionId O identificador da transação (pode ser um ID de MFA ou o próprio userId).
     * @param type O tipo de desafio (OTP, Biometria, etc).
     * @param answer Mapa de respostas (ex: ["otp": "123456"]).
     */
    fun resolveChallenge(
        transactionId: String, 
        type: ChallengeType, 
        answer: Map<String, String>
    ): Flow<ChallengeResult>
    
    /**
     * Solicita o reenvio de um desafio.
     */
    fun resendChallenge(transactionId: String, type: ChallengeType): Flow<Boolean>
}
