package com.diegoferreiracaetano.dlearn.ui.viewmodel

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher

/** Junta os efeitos recebidos desde já; a coleta termina junto com o teste. */
@OptIn(ExperimentalCoroutinesApi::class)
fun <E> TestScope.collectEffects(effects: Flow<E>): List<E> {
    val received = mutableListOf<E>()
    backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
        effects.collect { received += it }
    }
    return received
}
