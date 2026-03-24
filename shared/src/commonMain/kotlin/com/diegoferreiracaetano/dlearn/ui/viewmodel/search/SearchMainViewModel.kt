package com.diegoferreiracaetano.dlearn.ui.viewmodel.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchMainViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchMain()
    }

    fun retry() {
        fetchMain()
    }

    private fun fetchMain() {
        viewModelScope.launch {
            searchRepository.getSearchMain()
                .onStart {
                    _uiState.value = UIState.Loading
                }
                .catch { error ->
                    _uiState.value = UIState.Error(error)
                }
                .collect { screen ->
                    _uiState.value = UIState.Success(screen)
                }
        }
    }
}
