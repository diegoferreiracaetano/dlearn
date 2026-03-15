package com.diegoferreiracaetano.dlearn.ui.screens.favorite

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class FavoriteViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadFavorite()
    }

    fun retry() {
        _uiState.value = UIState.Loading
        loadFavorite()
    }

    private fun loadFavorite() {
        viewModelScope.launch {

        }
    }
}
