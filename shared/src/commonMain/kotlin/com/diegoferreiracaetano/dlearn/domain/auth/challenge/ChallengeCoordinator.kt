package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import com.diegoferreiracaetano.dlearn.util.event.GlobalEvent
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Coordenador que orquestra a comunicação entre os Handlers de desafio e a UI.
 * Mantém o estado interno do desafio atual para que a UI não precise lidar com IDs.
 */
class ChallengeCoordinator(
    private val globalEventDispatcher: GlobalEventDispatcher
) {
    private val _challenges = MutableSharedFlow<ChallengeSession>(extraBufferCapacity = 1)
    val challenges = _challenges.asSharedFlow()

    private var _currentSession: ChallengeSession? = null
    val currentSession: ChallengeSession? get() = _currentSession

    private var _activeChallenge: Challenge? = null
    val activeChallenge: Challenge? get() = _activeChallenge

    /**
     * Emite uma nova sessão de desafio e armazena internamente o contexto.
     * @param session A sessão completa recebida do servidor.
     * @param challenge O desafio específico que está sendo processado no momento.
     */
    suspend fun emit(session: ChallengeSession, challenge: Challenge) {
        println("ChallengeCoordinator: Storing Context - Session: ${session.transactionId}, Challenge: ${challenge.challengeType}")
        _currentSession = session
        _activeChallenge = challenge
        
        // Emite no flow específico (usado pelos handlers)
        _challenges.emit(session)
        
        // Dispara o evento global capturado pelo AppNavGraph/UI Root
        globalEventDispatcher.emit(GlobalEvent.Challenge(session))
    }

    /**
     * Limpa a sessão atual após a resolução ou cancelamento.
     */
    fun clear() {
        _currentSession = null
        _activeChallenge = null
    }
}
