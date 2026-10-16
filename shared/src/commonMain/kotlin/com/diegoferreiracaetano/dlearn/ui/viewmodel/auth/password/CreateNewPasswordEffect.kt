package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password

import com.diegoferreiracaetano.dlearn.domain.error.AppError

sealed interface CreateNewPasswordEffect {
    data class Changed(
        val message: String,
    ) : CreateNewPasswordEffect

    data class ShowError(
        val error: AppError,
    ) : CreateNewPasswordEffect
}
