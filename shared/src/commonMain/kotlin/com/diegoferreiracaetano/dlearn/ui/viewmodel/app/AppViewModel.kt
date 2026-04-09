package com.diegoferreiracaetano.dlearn.ui.viewmodel.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class AppViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private var lastRequest: AppRequest? = null

    fun fetch(
        path: String,
        params: Map<String, String>? = null,
    ) {
        _uiState.value = UIState.Loading
        fetch(AppPath.parse(path, params))
    }

    fun action(
        action: String
    ) {
        fetch(AppPath.parse(action))
    }

    fun retry() {
        lastRequest?.let { request ->
            fetch(request)
        }
    }

    fun fetch(request: AppRequest) {
        lastRequest = request
        viewModelScope.launch {
            repository
                .execute(request)
                .catch { e -> _uiState.value = UIState.Error(e) }
                .collect { screen ->
                    _uiState.value = UIState.Success(screen)
                }
        }
    }
}
