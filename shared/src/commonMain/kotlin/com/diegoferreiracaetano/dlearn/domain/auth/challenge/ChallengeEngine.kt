package com.diegoferreiracaetano.dlearn.domain.auth.challenge

/**
 * Motor de desafios que orquestra os handlers disponíveis.
 */
class ChallengeEngine(
    private val coordinator: ChallengeCoordinator,
    private val handlers: List<ChallengeHandler>,
) {
    /**
     * Resolve uma sessão de desafio utilizando o handler apropriado.
     */
    suspend fun resolve(session: ChallengeSession): ChallengeResult {
        val challenge = session.challenge

        val handler =
            handlers.find { it.canHandle(challenge) }
                ?: return ChallengeResult.Failure(
                    IllegalStateException("Nenhum handler encontrado para o desafio: ${challenge.challengeType}"),
                )

        // O coordinator armazena a sessão e o desafio ativo para uso posterior pelo repositório
        return coordinator.run {
            emit(session, challenge)
            handler.handle(challenge, session)
        }
    }
}
