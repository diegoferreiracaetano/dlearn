package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

sealed interface SignUpUIState {
    data object Idle : SignUpUIState

    data object Loading : SignUpUIState
}
