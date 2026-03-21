package com.diegoferreiracaetano.dlearn.data.challenge

import com.diegoferreiracaetano.dlearn.domain.challenge.*
import kotlinx.coroutines.CompletableDeferred

/**
 * Implementação do Handler de OTP que se comunica com o ChallengeCoordinator.
 */
class OtpChallengeHandler(
    private val coordinator: ChallengeCoordinator
) : ChallengeHandler {

    private var currentDeferred: CompletableDeferred<ChallengeResult>? = null

    override fun canHandle(challenge: Challenge): Boolean {
        return challenge.challengeType == ChallengeType.OTP_SMS || challenge.challengeType == ChallengeType.OTP_EMAIL
    }

    override suspend fun handle(challenge: Challenge, session: ChallengeSession): ChallengeResult {
        val deferred = CompletableDeferred<ChallengeResult>()
        currentDeferred = deferred

        val request = ChallengeRequest(
            type = challenge.challengeType,
            session = session,
            challenge = challenge,
            resolver = deferred
        )

        // Emite para o Coordenador Global. O App Root ou NavGraph estará observando isso.
        coordinator.emit(request)

        return try {
            deferred.await()
        } finally {
            currentDeferred = null
        }
    }

    /**
     * Chamado quando o OTP é validado com sucesso pela UI/UseCase.
     */
    fun onChallengeResolved(token: String) {
        currentDeferred?.complete(ChallengeResult.Success(mapOf("X-Challenge-Token" to token)))
    }

    /**
     * Chamado se o usuário cancelar o fluxo.
     */
    fun onChallengeCancelled() {
        currentDeferred?.complete(ChallengeResult.Cancelled)
    }
}
