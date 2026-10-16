package com.diegoferreiracaetano.dlearn.ui.viewmodel.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.AppRepository
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.ScreenLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class AppViewModel(
    private val repository: AppRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private val loader = ScreenLoader(viewModelScope, _uiState)

    fun onAction(action: AppAction) {
        when (action) {
            is AppAction.Load -> {
                val request = AppPath.parse(action.path, action.params)
                loader.load { repository.execute(request) }
            }

            is AppAction.Open -> {
                val request = AppPath.parse(action.url)
                loader.refresh { repository.execute(request) }
            }

            AppAction.Retry -> loader.retry()
        }
    }
}
