package com.diegoferreiracaetano.dlearn.data.auth.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType

/**
 * Handler genérico para desafios de Biometria.
 * A implementação real da biometria deve ser injetada via [BiometricProvider].
 */
class BiometricChallengeHandler(
    private val biometricProvider: BiometricProvider
) : ChallengeHandler {

    override fun canHandle(challenge: Challenge): Boolean {
        return challenge.challengeType == ChallengeType.BIOMETRIC
    }

    override suspend fun handle(challenge: Challenge, session: ChallengeSession): ChallengeResult {
        return try {
            val result = biometricProvider.authenticate()
            if (result is BiometricResult.Success) {
                // Em um cenário real, o token viria da validação da biometria com o backend
                // ou de um Keystore local que assina a transação.
                ChallengeResult.Success(mapOf("X-Challenge-Token" to result.token))
            } else {
                ChallengeResult.Cancelled
            }
        } catch (e: Exception) {
            ChallengeResult.Failure(e)
        }
    }
}

interface BiometricProvider {
    suspend fun authenticate(): BiometricResult
}

sealed interface BiometricResult {
    data class Success(val token: String) : BiometricResult
    data object Failure : BiometricResult
}
