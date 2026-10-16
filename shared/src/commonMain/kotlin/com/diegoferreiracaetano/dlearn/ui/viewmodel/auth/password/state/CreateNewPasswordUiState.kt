package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password.state

sealed interface CreateNewPasswordUiState {
    data object Idle : CreateNewPasswordUiState

    data object Loading : CreateNewPasswordUiState
}
