package com.diegoferreiracaetano.dlearn.ui.viewmodel.settings

sealed interface SettingsAction {
    data class Load(
        val path: String,
    ) : SettingsAction

    data class UpdatePreference(
        val key: String,
        val value: String,
    ) : SettingsAction

    data object Retry : SettingsAction
}
