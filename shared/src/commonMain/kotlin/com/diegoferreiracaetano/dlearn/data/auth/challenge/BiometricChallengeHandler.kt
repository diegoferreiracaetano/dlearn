package com.diegoferreiracaetano.dlearn.data.auth.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType

/**
 * Handler genérico para desafios de Biometria.
 * TODO: Implementar BiometricProvider nativo (iOS/Android) para capturar a credencial local.
 */
class BiometricChallengeHandler(
    private val biometricProvider: BiometricProvider,
) : ChallengeHandler {
    override fun canHandle(challenge: Challenge): Boolean = challenge.challengeType == ChallengeType.BIOMETRIC

    override suspend fun handle(
        challenge: Challenge,
        session: ChallengeSession,
    ): ChallengeResult =
        try {
            // Chama a interface que será implementada via Expect/Actual ou Injeção Nativa
            val result = biometricProvider.authenticate()
            if (result is BiometricResult.Success) {
                // Padronizado para "validatedToken" para que o Interceptor possa repetir a requisição original
                ChallengeResult.Success(mapOf("validatedToken" to result.token))
            } else {
                ChallengeResult.Cancelled
            }
        } catch (e: Exception) {
            ChallengeResult.Failure(e)
        }
}

/**
 * Interface para abstração da Biometria nativa.
 */
interface BiometricProvider {
    suspend fun authenticate(): BiometricResult
}

sealed interface BiometricResult {
    data class Success(
        val token: String,
    ) : BiometricResult

    data object Failure : BiometricResult
}
