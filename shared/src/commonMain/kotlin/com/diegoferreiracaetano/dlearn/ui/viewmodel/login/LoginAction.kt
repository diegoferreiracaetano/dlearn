package com.diegoferreiracaetano.dlearn.ui.viewmodel.login

import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider

sealed interface LoginAction {
    data class Submit(
        val email: String,
        val password: String,
    ) : LoginAction

    data class SocialSignIn(
        val provider: AccountProvider,
    ) : LoginAction

    data object CheckSession : LoginAction
}
