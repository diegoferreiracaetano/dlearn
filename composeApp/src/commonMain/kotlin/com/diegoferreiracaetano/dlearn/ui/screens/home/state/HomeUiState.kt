package com.diegoferreiracaetano.dlearn.ui.screens.home.state

import com.diegoferreiracaetano.dlearn.domain.home.Home

sealed interface HomeUiState {
    data object Loading : HomeUiState
    data class Success(val home: Home) : HomeUiState
    data class Error(val message: String?) : HomeUiState
}
