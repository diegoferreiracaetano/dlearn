package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state

sealed class CreateNewPasswordUiState {
    data object Idle : CreateNewPasswordUiState()
    data object Loading : CreateNewPasswordUiState()
    data class Success(val message: String) : CreateNewPasswordUiState()
    data class Error(val message: String) : CreateNewPasswordUiState()
}
