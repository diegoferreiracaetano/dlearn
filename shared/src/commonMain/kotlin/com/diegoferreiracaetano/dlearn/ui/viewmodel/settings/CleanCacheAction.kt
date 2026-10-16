package com.diegoferreiracaetano.dlearn.ui.viewmodel.settings

sealed interface CleanCacheAction {
    data object Confirm : CleanCacheAction
}
