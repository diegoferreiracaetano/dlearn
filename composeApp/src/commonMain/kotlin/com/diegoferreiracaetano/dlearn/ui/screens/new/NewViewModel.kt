package com.diegoferreiracaetano.dlearn.ui.screens.new

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.screens.new.state.NewUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NewUiState>(NewUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadNew()
    }

    fun retry() {
        _uiState.value = NewUiState.Loading
        loadNew()
    }

    private fun loadNew() {
        viewModelScope.launch {
            try {
                mainRepository.getContent("new").collect { screen ->
                    _uiState.value = NewUiState.Success(screen)
                }
            } catch (e: Exception) {
                _uiState.value = NewUiState.Error(e)
            }
        }
    }
}
