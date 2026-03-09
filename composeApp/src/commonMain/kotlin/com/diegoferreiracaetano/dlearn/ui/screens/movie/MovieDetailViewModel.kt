package com.diegoferreiracaetano.dlearn.ui.screens.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.ui.screens.movie.state.MovieDetailUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: String,
    private val repository: MovieDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<MovieDetailUiState>(MovieDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchMovieDetail()
    }

    fun retry() {
        fetchMovieDetail()
    }

    private fun fetchMovieDetail() {
        _uiState.value = MovieDetailUiState.Loading
        viewModelScope.launch {
            repository.getMovieDetail(movieId)
                .catch { e ->
                    _uiState.value = MovieDetailUiState.Error(e)
                }
                .collect { screen ->
                    _uiState.value = MovieDetailUiState.Success(screen)
                }
        }
    }
}
