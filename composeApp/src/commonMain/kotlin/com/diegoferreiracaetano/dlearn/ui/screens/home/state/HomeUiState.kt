package com.diegoferreiracaetano.dlearn.ui.screens.home.state

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val screen: Screen) : HomeUiState
    data class Error(val message: String?) : HomeUiState
}
