package com.diegoferreiracaetano.dlearn.ui.util

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf

class AppContainerState {
    var isLoading by mutableStateOf(false)
        private set

    var error by mutableStateOf<Throwable?>(null)
        private set

    var onRetry by mutableStateOf<(() -> Unit)?>(null)
        private set

    fun update(
        isLoading: Boolean = false,
        error: Throwable? = null,
        onRetry: (() -> Unit)? = null
    ) {
        this.isLoading = isLoading
        this.error = error
        this.onRetry = onRetry
    }

    fun reset() {
        update(isLoading = false, error = null, onRetry = null)
    }
}

val LocalAppContainerState = staticCompositionLocalOf { AppContainerState() }
