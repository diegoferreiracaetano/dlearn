package com.diegoferreiracaetano.dlearn.domain.auth.challenge

import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Coordenador que orquestra a comunicação entre os Handlers de desafio e a UI.
 */
class ChallengeCoordinator(
    private val globalEventDispatcher: GlobalEventDispatcher
) {
    private val _challenges = MutableSharedFlow<ChallengeSession>(extraBufferCapacity = 1)
    val challenges = _challenges.asSharedFlow()

    /**
     * Emite uma nova sessão de desafio para ser resolvida pela UI.
     */
    suspend fun emit(session: ChallengeSession) {
        _challenges.emit(session)
    }
}
