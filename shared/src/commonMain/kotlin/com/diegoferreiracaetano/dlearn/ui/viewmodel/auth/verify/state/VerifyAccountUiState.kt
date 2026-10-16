package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state

import com.diegoferreiracaetano.dlearn.domain.error.AppError

sealed interface VerifyAccountUiState {
    data object Idle : VerifyAccountUiState

    data object Loading : VerifyAccountUiState

    data class Error(
        val error: AppError,
    ) : VerifyAccountUiState
}
