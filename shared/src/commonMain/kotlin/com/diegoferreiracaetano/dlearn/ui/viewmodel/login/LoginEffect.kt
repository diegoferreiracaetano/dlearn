package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

import com.diegoferreiracaetano.dlearn.domain.error.AppError

sealed interface LoginEffect {
    data object NavigateToHome : LoginEffect

    data class ShowError(
        val error: AppError,
    ) : LoginEffect
}
