package com.diegoferreiracaetano.dlearn.ui.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.screens.main.state.MainUiState
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
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText.asStateFlow()

    private val _isSearchVisible = MutableStateFlow(false)
    val isSearchVisible: StateFlow<Boolean> = _isSearchVisible.asStateFlow()

    init {
        loadMain()
    }

    fun loadMain() {
        viewModelScope.launch {
            mainRepository.getMain().onStart {
                _uiState.update { UIState.Loading }
            }.catch { error ->
                _uiState.update { UIState.Error(error) }
            }.collect { screen ->
                _uiState.update { UIState.Success(screen) }
            }
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
