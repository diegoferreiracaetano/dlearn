package com.diegoferreiracaetano.dlearn.ui.screens.search.state

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

sealed interface SearchUiState {
    data object Loading : SearchUiState
    data class Success(val screen: Screen) : SearchUiState
    data class Error(val throwable: Throwable) : SearchUiState
}
