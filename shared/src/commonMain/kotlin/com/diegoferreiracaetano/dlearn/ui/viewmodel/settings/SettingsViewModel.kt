package com.diegoferreiracaetano.dlearn.ui.viewmodel.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class SettingsViewModel(
    private val appRepository: AppRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private var currentPath: String = ""

    init {
        viewModelScope.launch {
            preferencesRepository.onConfigurationChanged.collect {
                if (currentPath.isNotEmpty()) {
                    loadContent(currentPath)
                }
            }
        }
    }

    fun loadContent(path: String) {
        currentPath = path
        execute(path)
    }

    fun retry() {
        if (currentPath.isNotEmpty()) {
            loadContent(currentPath)
        }
    }

    fun updatePreference(key: String, value: String) {
        preferencesRepository.updatePreference(key, value)
    }

    private fun execute(path: String) {
        viewModelScope.launch {
            appRepository.execute(path = path).onStart {
                _uiState.update { UIState.Loading }
            }.catch { error ->
                _uiState.update { UIState.Error(error) }
            }.collect { screen ->
                _uiState.update { UIState.Success(screen) }
            }
        }
    }
}
