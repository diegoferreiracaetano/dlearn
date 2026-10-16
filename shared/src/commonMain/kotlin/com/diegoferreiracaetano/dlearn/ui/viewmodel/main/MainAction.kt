package com.diegoferreiracaetano.dlearn.ui.viewmodel.main

sealed interface MainAction {
    data class SelectTab(
        val route: String,
    ) : MainAction

    data object Retry : MainAction
}
