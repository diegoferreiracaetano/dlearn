package com.diegoferreiracaetano.dlearn.ui.screens.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.search.SearchRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.*
import com.diegoferreiracaetano.dlearn.ui.screens.search.state.SearchUiState
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

@OptIn(FlowPreview::class)
class SearchViewModel(
    private val searchRepository: SearchRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<SearchUiState>(SearchUiState.Loading)
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()

    private val _contentState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val contentState: StateFlow<UIState<Screen>> = _contentState.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private var searchJob: Job? = null

    init {
        loadShell()
        setupSearchDebounce()
    }

    private fun loadShell() {
        viewModelScope.launch {
            searchRepository.getSearchShell(_query.value)
                .catch { error ->
                    _uiState.value = SearchUiState.Error(error)
                }
                .collect { screen ->
                    // Optionally extract the query from the server's shell configuration
                    val searchBar = screen.components.firstOrNull() as? AppSearchBarComponent
                    if (searchBar != null && searchBar.query.isNotEmpty() && _query.value.isEmpty()) {
                        _query.value = searchBar.query
                    }
                    _uiState.value = SearchUiState.Success(screen)
                }
        }
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

    private fun executeSearch(query: String) {
        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            searchRepository.search(query)
                .onStart {
                    // _contentState.value = UIState.Loading
                }
                .catch { throwable ->
                    _contentState.value = UIState.Error(throwable)
                }
                .collect { screen ->
                    _contentState.value = UIState.Success(screen)
                }
        }
    }

    fun retry() {
        if (_uiState.value is SearchUiState.Error) {
            loadShell()
        }
        executeSearch(_query.value)
    }
}
