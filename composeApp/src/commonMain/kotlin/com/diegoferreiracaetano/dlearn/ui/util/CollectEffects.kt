package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import kotlinx.coroutines.flow.Flow

/**
 * Coleta os efeitos de uma vez só de um ViewModel (veja `EffectChannel`).
 * O `onEffect` mais recente é sempre o usado, sem reiniciar a coleta.
 */
@Composable
fun <E> CollectEffects(
    effects: Flow<E>,
    onEffect: suspend (E) -> Unit,
) {
    val currentOnEffect by rememberUpdatedState(onEffect)

    LaunchedEffect(effects) {
        effects.collect { currentOnEffect(it) }
    }
}
