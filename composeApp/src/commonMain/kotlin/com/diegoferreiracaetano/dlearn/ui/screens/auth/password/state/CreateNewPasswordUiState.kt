package com.diegoferreiracaetano.dlearn.ui.screens.auth.password.state

sealed class CreateNewPasswordUiState {
    object Idle : CreateNewPasswordUiState()
    object Loading : CreateNewPasswordUiState()
    data class Success(val message: String) : CreateNewPasswordUiState()
    data class Error(val message: String) : CreateNewPasswordUiState()
}
