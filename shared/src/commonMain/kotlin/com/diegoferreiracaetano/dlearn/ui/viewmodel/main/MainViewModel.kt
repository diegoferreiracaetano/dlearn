package com.diegoferreiracaetano.dlearn.ui.viewmodel.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.app.PreferencesRepository
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
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

    private val mainTabs = listOf(
        AppNavigationRoute.HOME,
        AppNavigationRoute.WATCHLIST,
        AppNavigationRoute.FAVORITE,
        AppNavigationRoute.PROFILE
    )

    init {
        loadMain()
        viewModelScope.launch {
            preferencesRepository.onConfigurationChanged.collect {
                loadMain()
            }
        }
    }

    fun loadMain() {
        viewModelScope.launch {
            mainRepository
                .getMain()
                .onStart {
                    _uiState.update { UIState.Loading }
                }.catch { error ->
                    _uiState.update { UIState.Error(error) }
                }.collect { screen ->
                    _uiState.update { UIState.Success(screen) }
                }
        }
    }

    fun onTabSelected(route: String, onNavigate: (String) -> Unit) {
        if (route in mainTabs) {
            _currentTab.update { route }
        } else {
            onNavigate(route)
        }
    }

    fun retry() {
        loadMain()
    }
}
