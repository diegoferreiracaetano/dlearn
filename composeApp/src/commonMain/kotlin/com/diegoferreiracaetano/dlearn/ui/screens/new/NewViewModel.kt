package com.diegoferreiracaetano.dlearn.ui.screens.new

import androidx.lifecycle.ViewModel
import com.diegoferreiracaetano.dlearn.domain.main.MainRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class NewViewModel(
    private val mainRepository: MainRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        loadNew()
    }

    fun retry() {
        loadNew()
    }

    private fun loadNew() {

    }
}
