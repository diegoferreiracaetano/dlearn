package com.diegoferreiracaetano.dlearn.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class SearchContentViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    private var searchJob: Job? = null

    fun fetchSearch(query: String) {
        searchJob?.cancel()

        searchJob = viewModelScope.launch {
            delay(500)
            _uiState.value = UIState.Loading
            searchRepository.search(query)
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
