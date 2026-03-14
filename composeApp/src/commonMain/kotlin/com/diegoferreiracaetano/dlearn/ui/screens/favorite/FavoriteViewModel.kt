package com.diegoferreiracaetano.dlearn.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.screens.favorite.state.FavoriteUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<FavoriteUiState>(FavoriteUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorite()
    }

    fun retry() {
        _uiState.value = FavoriteUiState.Loading
        loadFavorite()
    }

    private fun loadFavorite() {
        viewModelScope.launch {
            try {
                mainRepository.getContent("favorite").collect { screen ->
                    _uiState.value = FavoriteUiState.Success(screen)
                }
            } catch (e: Exception) {
                _uiState.value = FavoriteUiState.Error(e)
            }
        }
    }
}
