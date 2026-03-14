package com.diegoferreiracaetano.dlearn.ui.screens.favorite.state

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

sealed interface FavoriteUiState {
    data object Loading : FavoriteUiState
    data class Success(val screen: Screen) : FavoriteUiState
    data class Error(val throwable: Throwable) : FavoriteUiState
}
