package com.diegoferreiracaetano.dlearn.ui.sdui

sealed interface UIState {
    data object Loading : UIState
    data class Success(val screen: Screen) : UIState
    data class Error(val throwable: Throwable) : UIState
}
