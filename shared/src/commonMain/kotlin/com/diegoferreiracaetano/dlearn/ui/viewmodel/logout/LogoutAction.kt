package com.diegoferreiracaetano.dlearn.ui.viewmodel.logout

sealed interface LogoutAction {
    data object Confirm : LogoutAction
}
