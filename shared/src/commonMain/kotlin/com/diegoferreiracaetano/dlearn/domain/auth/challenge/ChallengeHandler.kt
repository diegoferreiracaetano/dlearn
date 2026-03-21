package com.diegoferreiracaetano.dlearn.domain.auth.challenge

/**
 * Interface para um tratador de desafio específico (ex: OTP, Biometria).
 */
interface ChallengeHandler {
    /** Verifica se este handler pode lidar com o desafio fornecido. */
    fun canHandle(challenge: Challenge): Boolean

    /** Processa o desafio e retorna o resultado. */
    suspend fun handle(challenge: Challenge, session: ChallengeSession): ChallengeResult
}
