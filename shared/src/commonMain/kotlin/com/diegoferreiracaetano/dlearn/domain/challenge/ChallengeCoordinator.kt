package com.diegoferreiracaetano.dlearn.domain.challenge

import com.diegoferreiracaetano.dlearn.util.event.GlobalEvent
import com.diegoferreiracaetano.dlearn.util.event.GlobalEventDispatcher
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Representa um pedido de desafio vindo da camada de rede/infraestrutura.
 */
data class ChallengeRequest(
    val type: ChallengeType,
    val session: ChallengeSession,
    val challenge: Challenge,
    val resolver: CompletableDeferred<ChallengeResult>
)

/**
 * Coordenador Global de Desafios.
 * Utiliza o GlobalEventDispatcher para desacoplar a emissão e mantém o estado do desafio ativo.
 */
class ChallengeCoordinator(
    private val eventDispatcher: GlobalEventDispatcher
) {
    private val _activeRequest = MutableStateFlow<ChallengeRequest?>(null)
    val activeRequest = _activeRequest.asStateFlow()

    /**
     * Emite um pedido de desafio via GlobalEvent.
     */
    suspend fun emit(request: ChallengeRequest) {
        _activeRequest.value = request
        eventDispatcher.emit(GlobalEvent.Challenge(request))
    }

    /**
     * Resolve o desafio ativo através do resultado fornecido pela UI.
     */
    fun resolve(result: ChallengeResult) {
        _activeRequest.value?.resolver?.complete(result)
        _activeRequest.value = null
    }
}
