package com.diegoferreiracaetano.dlearn.data.auth.challenge

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import kotlinx.coroutines.CompletableDeferred

/**
 * Implementação do Handler de OTP que se comunica com o ChallengeCoordinator.
 */
class OtpChallengeHandler : ChallengeHandler {
    private var currentDeferred: CompletableDeferred<ChallengeResult>? = null

    override fun canHandle(challenge: Challenge): Boolean =
        challenge.challengeType == ChallengeType.OTP_SMS || challenge.challengeType == ChallengeType.OTP_EMAIL

    override suspend fun handle(
        challenge: Challenge,
        session: ChallengeSession,
    ): ChallengeResult {
        val deferred = CompletableDeferred<ChallengeResult>()
        currentDeferred = deferred

        return try {
            deferred.await()
        } finally {
            currentDeferred = null
        }
    }

    /**
     * Chamado quando o OTP é validado com sucesso pela UI/UseCase.
     * @param token O validatedToken retornado pelo endpoint /resolve do backend.
     */
    fun onChallengeResolved(token: String) {
        // A chave "validatedToken" deve bater com o esperado pelo ChallengeInterceptor
        currentDeferred?.complete(ChallengeResult.Success(mapOf("validatedToken" to token)))
    }

    /**
     * Chamado se o usuário cancelar o fluxo.
     */
    fun onChallengeCancelled() {
        currentDeferred?.complete(ChallengeResult.Cancelled)
    }
}
