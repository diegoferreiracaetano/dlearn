package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.home.HomeRepository
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: HomeRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentType = HomeFilterType.ALL
    private var currentSearch: String? = null
    private var searchJob: Job? = null

    init {
        fetchHome()
    }

    fun onSearchChanged(query: String?) {
        if (currentSearch == query) return
        currentSearch = query

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(500)
            fetchHome()
        }
    }

    fun onFilterTypeChanged(type: String?) {
        val filterType = when (type) {
            "Séries" -> HomeFilterType.SERIES
            "Filmes" -> HomeFilterType.MOVIE
            else -> HomeFilterType.ALL
        }
        if (currentType == filterType) return
        currentType = filterType
        fetchHome()
    }

    private fun fetchHome() {
        _uiState.value = HomeUiState.Loading
        viewModelScope.launch {
            repository.getHome(type = currentType, search = currentSearch)
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message)
                }
                .collect { home ->
                    _uiState.value = HomeUiState.Success(home)
                }
        }
    }
}
