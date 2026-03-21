package com.diegoferreiracaetano.dlearn.ui.screens.auth.verify.state

sealed class VerifyAccountUiState {
    object Idle : VerifyAccountUiState()
    object Loading : VerifyAccountUiState()
    object Success : VerifyAccountUiState()
    data class Error(val message: String) : VerifyAccountUiState()
}
