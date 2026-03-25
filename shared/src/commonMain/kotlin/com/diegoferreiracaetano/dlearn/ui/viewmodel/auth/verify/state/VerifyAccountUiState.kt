package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify.state

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult

sealed class VerifyAccountUiState {
    data object Idle : VerifyAccountUiState()
    data object Loading : VerifyAccountUiState()
    data object Success : VerifyAccountUiState()
    data class Error(val throwable: Throwable) : VerifyAccountUiState()
}

/**
 * Extension to map ChallengeResult to VerifyAccountUiState
 */
fun ChallengeResult.toUiState(): VerifyAccountUiState = when (this) {
    is ChallengeResult.Success -> VerifyAccountUiState.Success
    is ChallengeResult.Failure -> VerifyAccountUiState.Error(error)
    is ChallengeResult.Cancelled -> VerifyAccountUiState.Idle
}
