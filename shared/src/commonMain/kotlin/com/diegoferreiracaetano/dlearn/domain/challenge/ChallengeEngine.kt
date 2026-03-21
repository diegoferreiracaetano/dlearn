package com.diegoferreiracaetano.dlearn.domain.challenge

class ChallengeEngine(
    private val handlers: List<ChallengeHandler>
) {
    suspend fun resolve(session: ChallengeSession): ChallengeResult {
        // Tenta resolver cada desafio da sessão em ordem
        for (challenge in session.challenges) {
            val handler = handlers.find { it.canHandle(challenge) }
            if (handler != null) {
                val result = handler.handle(challenge, session)
                if (result is ChallengeResult.Success) {
                    return result
                }
                // Se falhou ou foi cancelado, tenta o próximo desafio (fallback) se houver
            }
        }
        return ChallengeResult.Failure(Exception("No handler could resolve the challenge session"))
    }
}
