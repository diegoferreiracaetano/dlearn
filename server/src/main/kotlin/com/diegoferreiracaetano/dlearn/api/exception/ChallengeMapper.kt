package com.diegoferreiracaetano.dlearn.api.exception

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType

/**
 * Mapper responsável por converter exceções de negócio em sessões de desafio (MFA).
 * Centraliza a lógica de tradução para o contrato do Challenge Engine.
 */
class ChallengeMapper {
    fun toChallengeSession(
        cause: Throwable,
        preferredType: ChallengeType? = null,
    ): ChallengeSession? =
        when (cause) {
            is ChallengeException -> {
                val type = preferredType ?: ChallengeType.OTP_EMAIL

                ChallengeSession(
                    transactionId = cause.error.challengeToken,
                    challenge =
                    Challenge(
                        challengeType = type,
                        data = mapOf("message" to cause.error.message),
                    ),
                )
            }

            else -> null
        }
}
