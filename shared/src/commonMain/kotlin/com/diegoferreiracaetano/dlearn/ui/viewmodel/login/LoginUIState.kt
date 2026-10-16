package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

sealed interface LoginUIState {
    data object Idle : LoginUIState

    data object Loading : LoginUIState
}
