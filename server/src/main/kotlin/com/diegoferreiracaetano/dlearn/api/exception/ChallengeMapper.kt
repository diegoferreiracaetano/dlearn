package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.domain.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeType
import com.diegoferreiracaetano.dlearn.orchestrator.PasswordChallengeException

/**
 * Mapper responsável por converter exceções de negócio em sessões de desafio (MFA).
 * Centraliza a lógica de tradução para o contrato do Challenge Engine.
 */
class ChallengeMapper {
    fun toChallengeSession(cause: Throwable): ChallengeSession? {
        return when (cause) {
            is PasswordChallengeException -> ChallengeSession(
                transactionId = cause.error.challengeToken,
                challenges = listOf(
                    Challenge(
                        challengeType = ChallengeType.OTP_EMAIL,
                        data = mapOf("message" to cause.error.message)
                    )
                )
            )
            else -> null
        }
    }
}
