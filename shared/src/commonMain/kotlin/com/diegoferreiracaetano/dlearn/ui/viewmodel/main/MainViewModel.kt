package com.diegoferreiracaetano.dlearn.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.ScreenLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository,
    private val preferencesRepository: PreferencesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private val _currentTab = MutableStateFlow(AppNavigationRoute.HOME)
    val currentTab: StateFlow<String> = _currentTab.asStateFlow()

    private val loader = ScreenLoader(viewModelScope, _uiState)

    init {
        loadMain()
        viewModelScope.launch {
            preferencesRepository.onConfigurationChanged.collect {
                loadMain()
            }
        }
    }

    fun onAction(action: MainAction) {
        when (action) {
            is MainAction.SelectTab -> _currentTab.update { action.route }
            MainAction.Retry -> loader.retry()
        }
    }

    private fun loadMain() {
        loader.load { mainRepository.getMain() }
    }
}
