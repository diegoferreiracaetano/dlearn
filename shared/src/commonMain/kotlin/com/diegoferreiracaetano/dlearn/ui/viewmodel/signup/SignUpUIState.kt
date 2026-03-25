package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

sealed class SignUpUIState {
    object Idle : SignUpUIState()
    object Loading : SignUpUIState()
    data class Success(val isLoggedIn: Boolean) : SignUpUIState()
    data class Error(val error: Throwable) : SignUpUIState()
}
