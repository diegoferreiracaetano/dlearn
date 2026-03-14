package com.diegoferreiracaetano.dlearn.ui.screens.main.state

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

sealed interface MainUiState {
    data object Loading : MainUiState
    data class Success(val screen: Screen) : MainUiState
    data class Error(val throwable: Throwable) : MainUiState
}
