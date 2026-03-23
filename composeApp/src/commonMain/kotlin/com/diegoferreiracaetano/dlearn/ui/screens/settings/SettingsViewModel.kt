package com.diegoferreiracaetano.dlearn.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.collectIn
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

class SettingsViewModel(
    private val repository: AppRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()
    
    private var lastRequest: AppRequest? = null

    init {
        preferencesRepository.onConfigurationChanged
            .onEach { 
                println("DEBUG: ViewModel - onConfigurationChanged triggered")
                retry() 
            }
            .launchIn(viewModelScope)
    }

    fun loadContent(path: String) {
        println("DEBUG: ViewModel - loadContent(path=$path)")
        execute(AppRequest(path))
    }

    fun retry() {
        println("DEBUG: ViewModel - retry() called")
        lastRequest?.let(::execute)
    }

    fun handleAction(action: String) {
        println("DEBUG: ViewModel - handleAction(action=$action)")
        execute(AppRequest(action))
    }

    fun updatePreference(key: String, value: String) {
        println("DEBUG: ViewModel - updatePreference(key=$key, value=$value)")
        preferencesRepository.updatePreference(key, value)
    }

    private fun execute(request: AppRequest) {
        println("DEBUG: ViewModel - execute(path=${request.path})")
        lastRequest = request
        repository.execute(request.path, request.params, request.metadata)
            .collectIn(viewModelScope, _uiState)
    }
}
