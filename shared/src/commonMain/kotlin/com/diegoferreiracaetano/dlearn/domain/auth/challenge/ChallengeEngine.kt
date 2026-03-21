package com.diegoferreiracaetano.dlearn.domain.auth.challenge

/**
 * Motor de desafios que orquestra os handlers disponíveis.
 */
class ChallengeEngine(
    private val handlers: List<ChallengeHandler>
) {
    /**
     * Resolve uma sessão de desafio iterando pelos handlers.
     */
    suspend fun resolve(session: ChallengeSession): ChallengeResult {
        // Pega o primeiro desafio da sessão
        val challenge = session.challenges.firstOrNull() ?: return ChallengeResult.Cancelled
        
        // Encontra um handler capaz de lidar com este desafio
        val handler = handlers.find { it.canHandle(challenge) }
            ?: return ChallengeResult.Failure(Exception("Nenhum handler encontrado para o desafio: ${challenge.challengeType}"))

        return handler.handle(challenge, session)
    }
}
