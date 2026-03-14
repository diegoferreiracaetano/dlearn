package com.diegoferreiracaetano.dlearn.ui.screens.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.screens.profile.state.ProfileUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ProfileUiState>(ProfileUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadProfile()
    }

    fun retry() {
        _uiState.value = ProfileUiState.Loading
        loadProfile()
    }

    private fun loadProfile() {
        viewModelScope.launch {
            try {
                mainRepository.getContent("person").collect { screen ->
                    _uiState.value = ProfileUiState.Success(screen)
                }
            } catch (e: Exception) {
                _uiState.value = ProfileUiState.Error(e)
            }
        }
    }
}
