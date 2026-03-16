package com.diegoferreiracaetano.dlearn.ui.screens.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private var lastRequest: AppRequest? = null

    fun loadContent(path: String, params: Map<String, String>? = null, metadata: Map<String, String>? = null) {
        val request = AppRequest(path, params, metadata)
        lastRequest = request
        executeRequest(request)
    }

    private fun executeRequest(request: AppRequest) {
        viewModelScope.launch {
            repository.execute(request.path, request.params, request.metadata)
                .onStart { _uiState.value = UIState.Loading }
                .catch { error -> _uiState.value = UIState.Error(error) }
                .collect { screen -> _uiState.value = UIState.Success(screen) }
        }
    }

    fun retry() {
        lastRequest?.let { executeRequest(it) }
    }
}
