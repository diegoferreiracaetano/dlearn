package com.diegoferreiracaetano.dlearn.util.event

import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow

/**
 * Sistema de Eventos Global para a aplicação.
 * Permite a comunicação desacoplada entre camadas (domain/infra -> UI Root).
 */
sealed interface GlobalEvent {
    data class Challenge(val request: ChallengeRequest) : GlobalEvent
    data class Message(val text: String, val type: MessageType = MessageType.INFO) : GlobalEvent
    data class Navigation(val route: String, val params: Map<String, String>? = null) : GlobalEvent
    
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
