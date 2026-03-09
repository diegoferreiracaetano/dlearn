package com.diegoferreiracaetano.dlearn.ui.screens.movie.state

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

sealed interface MovieDetailUiState {
    object Loading : MovieDetailUiState
    data class Success(val screen: Screen) : MovieDetailUiState
    data class Error(val throwable: Throwable) : MovieDetailUiState
}
