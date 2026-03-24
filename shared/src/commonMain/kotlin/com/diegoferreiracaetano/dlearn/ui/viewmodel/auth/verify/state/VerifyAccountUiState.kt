package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state

sealed class VerifyAccountUiState {
    data object Idle : VerifyAccountUiState()
    data object Loading : VerifyAccountUiState()
    data object Success : VerifyAccountUiState()
    data class Error(val message: String) : VerifyAccountUiState()
}
