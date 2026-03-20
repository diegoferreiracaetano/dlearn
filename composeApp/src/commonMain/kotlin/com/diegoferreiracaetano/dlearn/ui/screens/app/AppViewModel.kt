package com.diegoferreiracaetano.dlearn.ui.screens.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: AppRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private var formData: Map<String, String> = emptyMap()
    private var lastRequest: AppRequest? = null

    fun loadContent(path: String, params: Map<String, String>? = null) {
        execute(AppRequest(path, params))
    }

    fun retry() {
        lastRequest?.let(::execute)
    }

    fun handleQuery(query: String) {
        val parts = query.split(":", limit = 2)
        if (parts.size == 2) {
            formData = formData + (parts[0] to parts[1])
        }
    }

    fun handleAction(path: String) {
        val params = formData
        execute(AppRequest(path, params))
        formData = emptyMap()
    }

    private fun execute(request: AppRequest) {
        lastRequest = request

        viewModelScope.launch {
            repository.execute(request.path, request.params, request.metadata)
                .onStart { _uiState.value = UIState.Loading }
                .catch { _uiState.value = UIState.Error(it) }
                .collect { _uiState.value = UIState.Success(it) }
        }
    }
}
