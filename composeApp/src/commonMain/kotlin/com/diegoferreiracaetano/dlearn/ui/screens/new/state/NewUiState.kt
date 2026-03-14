package com.diegoferreiracaetano.dlearn.ui.screens.new.state

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

sealed interface NewUiState {
    data object Loading : NewUiState
    data class Success(val screen: Screen) : NewUiState
    data class Error(val throwable: Throwable) : NewUiState
}
