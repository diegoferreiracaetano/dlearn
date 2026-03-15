package com.diegoferreiracaetano.dlearn.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.util.getLogger
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private val _query = MutableStateFlow("")
    private var searchJob: Job? = null

    init {
        setupSearchDebounce()
    }

    private fun setupSearchDebounce() {
        viewModelScope.launch {
            _query
                .debounce(500L)
                .distinctUntilChanged()
                .collectLatest { query ->
                    executeSearch(query)
                }
        }
    }

    fun onQueryChange(query: String) {
        _query.value = query
    }

    fun onSearch(query: String) {
        // Implementação de busca imediata ao pressionar Enter (opcional)
        // Comentado conforme solicitado
        // _query.value = query
        // executeSearch(query)
    }

    private fun executeSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchRepository.search(query)
                .onStart {
                    // Opcional: Mostrar loading se desejar feedback visual durante a digitação
                    // _uiState.value = UIState.Loading
                }
                .catch { throwable ->
                    _uiState.value = UIState.Error(throwable)
                }
                .collect { screen ->

                    getLogger().d("VIEWMODEL", (screen.components as? AppSearchBarComponent)?.components.toString())
                    _uiState.value = UIState.Success(screen)
                }
        }
    }

    fun retry() {
        executeSearch(_query.value)
    }
}
