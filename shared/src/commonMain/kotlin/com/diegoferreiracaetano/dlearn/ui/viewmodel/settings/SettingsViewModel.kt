package com.diegoferreiracaetano.dlearn.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.ScreenLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val appRepository: AppRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private val loader = ScreenLoader(viewModelScope, _uiState)

    private var currentPath: String = ""

    init {
        viewModelScope.launch {
            preferencesRepository.onConfigurationChanged.collect {
                if (currentPath.isNotEmpty()) {
                    load(currentPath)
                }
            }
        }
    }

    fun onAction(action: SettingsAction) {
        when (action) {
            is SettingsAction.Load -> load(action.path)
            is SettingsAction.UpdatePreference ->
                preferencesRepository.updatePreference(action.key, action.value)

            SettingsAction.Retry -> loader.retry()
        }
    }

    private fun load(path: String) {
        currentPath = path
        val request = AppPath.parse(path)
        loader.load { appRepository.execute(request) }
    }
}
