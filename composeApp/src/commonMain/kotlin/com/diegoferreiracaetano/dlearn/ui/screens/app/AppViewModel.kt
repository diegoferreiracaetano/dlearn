package com.diegoferreiracaetano.dlearn.ui.screens.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.util.collectIn
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: AppRepository,
    private val preferencesRepository: PreferencesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private var formData: Map<String, String> = emptyMap()
    private var lastPath: String? = null
    private var lastParams: Map<String, String>? = null

    init {
        observeConfigurationChanges()
    }

    private fun observeConfigurationChanges() {
        viewModelScope.launch {
            preferencesRepository.onConfigurationChanged.collect {
                retry()
            }
        }
    }

    fun loadContent(path: String, params: Map<String, String>? = null) {
        execute(path, params)
    }

    fun retry() {
        lastPath?.let { path ->
            execute(path, lastParams)
        }
    }

    fun handleQuery(query: String) {
        val parts = query.split(":", limit = 2)
        if (parts.size == 2) {
            formData = formData + (parts[0] to parts[1])
        }
    }

    fun handleAction(path: String) {
        val params = formData
        execute(path, params)
        formData = emptyMap()
    }

    private fun execute(path: String, params: Map<String, String>? = null) {
        lastPath = path
        lastParams = params
        repository.execute(path = path, params = params)
            .collectIn(viewModelScope, _uiState)
    }
}
