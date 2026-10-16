package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.verify

sealed interface VerifyAccountAction {
    data class VerifyOtp(
        val code: String,
    ) : VerifyAccountAction

    data object ResendOtp : VerifyAccountAction

    data object Cancel : VerifyAccountAction
}
