package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchHome()
    }

    private fun fetchHome() {
        viewModelScope.launch {
            repository.getHome()
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message)
                }
                .collect { home ->
                    _uiState.value = HomeUiState.Success(home)
                }
        }
    }
}
