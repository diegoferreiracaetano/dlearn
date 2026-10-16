package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

import com.diegoferreiracaetano.dlearn.domain.error.AppError

sealed interface SignUpEffect {
    data object NavigateToHome : SignUpEffect

    data class ShowError(
        val error: AppError,
    ) : SignUpEffect
}
