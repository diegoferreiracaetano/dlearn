package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.NavigationRoutes
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.screens.main.state.MainUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MainUiState>(MainUiState.Loading)
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    private val _currentRoute = MutableStateFlow(NavigationRoutes.HOME)
    val currentRoute: StateFlow<String> = _currentRoute.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible: StateFlow<Boolean> = _isSearchVisible.asStateFlow()

    init {
        loadMain()
    }

    fun loadMain() {
        viewModelScope.launch {
            mainRepository.getMain(
                route = _currentRoute.value
            ).onStart {
                _uiState.update { MainUiState.Loading }
            }.catch { error ->
                _uiState.update { MainUiState.Error(error) }
            }.collect { screen ->
                _uiState.update { MainUiState.Success(screen) }
            }
        }
    }

    fun onTabSelected(route: String) {
        if (_currentRoute.value != route) {
            _currentRoute.value = route
            _searchText.value = ""
            _isSearchVisible.value = false
            loadMain()
        }
    }

    fun onShowSearchChanged(visible: Boolean) {
        _isSearchVisible.value = visible
    }

    fun onSearchTextChanged(text: String) {
        _searchText.value = text
    }

    fun retry() {
        loadMain()
    }
}
