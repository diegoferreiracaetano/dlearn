package com.diegoferreiracaetano.dlearn.ui.viewmodel

import com.diegoferreiracaetano.dlearn.domain.error.toAppError
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

/**
 * Único caminho de carregamento de telas SDUI: sempre passa por `Loading`,
 * guarda a última fonte (para o `retry` repetir o pedido de verdade) e cancela
 * a busca anterior antes de começar outra.
 */
class ScreenLoader(
    private val scope: CoroutineScope,
    private val state: MutableStateFlow<UIState<Screen>>,
) {
    private var job: Job? = null
    private var lastSource: (() -> Flow<Screen>)? = null

    /** Carrega mostrando `Loading` (primeira carga, troca de tela, nova tentativa). */
    fun load(source: () -> Flow<Screen>) = start(source, showLoading = true)

    /** Recarrega mantendo o conteúdo atual na tela até chegar o novo. */
    fun refresh(source: () -> Flow<Screen>) = start(source, showLoading = false)

    fun retry() {
        lastSource?.let { load(it) }
    }

    private fun start(
        source: () -> Flow<Screen>,
        showLoading: Boolean,
    ) {
        lastSource = source
        job?.cancel()
        job =
            scope.launch {
                source()
                    .onStart { if (showLoading) state.value = UIState.Loading }
                    .catch { state.value = UIState.Error(it.toAppError()) }
                    .collect { state.value = UIState.Success(it) }
            }
    }
}
