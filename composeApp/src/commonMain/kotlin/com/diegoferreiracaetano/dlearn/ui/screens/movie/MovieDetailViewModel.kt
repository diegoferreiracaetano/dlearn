package com.diegoferreiracaetano.dlearn.ui.screens.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: String,
    private val repository: MovieDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchMovieDetail()
    }

    fun retry() {
        fetchMovieDetail()
    }

    private fun fetchMovieDetail() {
        _uiState.value = UIState.Loading
        viewModelScope.launch {
            repository.getMovieDetail(movieId)
                .onStart {
                    _uiState.value = UIState.Loading
                }
                .catch { e ->
                    _uiState.value = UIState.Error(e)
                }
                .collect { screen ->
                    _uiState.value = UIState.Success(screen)
                }
        }
    }
}
