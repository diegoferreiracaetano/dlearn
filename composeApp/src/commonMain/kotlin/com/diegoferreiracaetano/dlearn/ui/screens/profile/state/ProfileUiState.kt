package com.diegoferreiracaetano.dlearn.ui.screens.profile.state

import com.diegoferreiracaetano.dlearn.ui.sdui.Screen

sealed interface ProfileUiState {
    data object Loading : ProfileUiState
    data class Success(val screen: Screen) : ProfileUiState
    data class Error(val throwable: Throwable) : ProfileUiState
}
