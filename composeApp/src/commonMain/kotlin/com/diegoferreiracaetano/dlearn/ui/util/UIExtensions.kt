package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

@Immutable
data class UiState<out T>(
    val success: T? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)

@OptIn(ExperimentalCoroutinesApi::class)
fun <T, R> Flow<T?>.asUiState(
    scope: CoroutineScope,
    initialState: UiState<R> = UiState(isLoading = false),
    transform: (T) -> Flow<R>
): StateFlow<UiState<R>> {
    return this
        .filterNotNull()
        .flatMapMerge { trigger ->
            transform(trigger)
                .map { result ->
                    UiState(success = result)
                }
                .onStart { emit(UiState(isLoading = true)) }
                .catch { e ->
                    emit(UiState(error = e.message ?: "Ocorreu um erro desconhecido"))
                }
        }
        .stateIn(
            scope = scope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = initialState
        )
}

fun <T, R> ViewModel.produceUiState(
    trigger: Flow<T?>,
    initialState: UiState<R> = UiState(),
    transform: (T) -> Flow<R>
): StateFlow<UiState<R>> {
    return trigger.asUiState(
        scope = this.viewModelScope,
        initialState = initialState,
        transform = transform
    )
}

fun Color.contrastTextColor(): Color {
    return if (this.luminance() > 0.5f) Color.Black else Color.White
}
