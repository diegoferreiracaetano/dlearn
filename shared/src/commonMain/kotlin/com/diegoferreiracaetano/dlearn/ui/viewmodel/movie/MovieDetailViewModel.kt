package com.diegoferreiracaetano.dlearn.ui.viewmodel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class MovieDetailViewModel(
    private val movieId: String,
    private val repository: MovieDetailRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchMovieDetail()
    }

    fun retry() {
        fetchMovieDetail()
    }

    private fun fetchMovieDetail() {
        viewModelScope.launch {
            repository.getMovieDetail(movieId)
                .catch { e -> _uiState.value = UIState.Error(e) }
                .collect { screen ->
                    _uiState.value = UIState.Success(screen)
                }
        }
    }
}
