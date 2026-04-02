package com.diegoferreiracaetano.dlearn.util.event

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Sistema de Eventos Global para a aplicação.
 * Permite a comunicação desacoplada entre camadas (domain/infra -> UI Root).
 */
sealed interface GlobalEvent {
    /**
     * Evento disparado quando um desafio de segurança (MFA) é exigido
     * e precisa ser interceptado pela UI principal.
     */
    data class Challenge(
        val session: ChallengeSession,
    ) : GlobalEvent

    data class Message(
        val text: String,
        val type: MessageType = MessageType.INFO,
    ) : GlobalEvent

    data class Navigation(
        val route: String,
        val params: Map<String, String>? = null,
    ) : GlobalEvent

    enum class MessageType { INFO, SUCCESS, ERROR, WARNING }
}

/**
 * Dispatcher central para emissão e observação de GlobalEvents.
 */
class GlobalEventDispatcher {
    private val _events = MutableSharedFlow<GlobalEvent>(extraBufferCapacity = 64)
    val events = _events.asSharedFlow()

    suspend fun emit(event: GlobalEvent) {
        _events.emit(event)
    }

    fun tryEmit(event: GlobalEvent) {
        _events.tryEmit(event)
    }
}
