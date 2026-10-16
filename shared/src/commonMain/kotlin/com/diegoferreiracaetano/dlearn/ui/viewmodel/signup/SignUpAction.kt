package com.diegoferreiracaetano.dlearn.ui.viewmodel.signup

sealed interface SignUpAction {
    data class Submit(
        val name: String,
        val email: String,
        val password: String,
    ) : SignUpAction
}
