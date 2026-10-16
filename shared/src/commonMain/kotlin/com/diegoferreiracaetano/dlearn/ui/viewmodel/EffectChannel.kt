package com.diegoferreiracaetano.dlearn.ui.viewmodel

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

/**
 * Canal de efeitos de uma tela (navegação, mensagem): acontecem uma vez só e
 * não fazem parte do estado. O efeito enviado sem ninguém coletando fica na
 * fila até a tela voltar a coletar, e cada efeito é entregue a um coletor só.
 */
class EffectChannel<E> {
    private val channel = Channel<E>(Channel.UNLIMITED)

    val effects: Flow<E> = channel.receiveAsFlow()

    fun send(effect: E) {
        channel.trySend(effect)
    }
}
