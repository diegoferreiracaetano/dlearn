package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.compose.runtime.staticCompositionLocalOf

interface MainContainerState {
    fun onMainLoading(loading: Boolean)
    fun onMainError(error: Throwable?)
}

val LocalMainContainerState = staticCompositionLocalOf<MainContainerState> {
    error("MainContainerState not provided")
}
