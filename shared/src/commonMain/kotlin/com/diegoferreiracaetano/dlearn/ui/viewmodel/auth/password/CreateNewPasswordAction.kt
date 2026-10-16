package com.diegoferreiracaetano.dlearn.ui.viewmodel.auth.password

sealed interface CreateNewPasswordAction {
    data class ChangePassword(
        val newPassword: String,
    ) : CreateNewPasswordAction
}
