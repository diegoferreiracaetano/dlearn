package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

sealed interface LoginUIState {
    data object Idle : LoginUIState

    data object Loading : LoginUIState

    data class Success(
        val isLoggedIn: Boolean,
    ) : LoginUIState

    data class Error(
        val throwable: Throwable,
    ) : LoginUIState
}
