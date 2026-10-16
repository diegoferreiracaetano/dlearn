package com.diegoferreiracaetano.dlearn.ui.viewmodel.movie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.domain.movie.MovieDetailRepository
import com.diegoferreiracaetano.dlearn.navigation.AppNavigationRoute
import com.diegoferreiracaetano.dlearn.navigation.AppPath
import com.diegoferreiracaetano.dlearn.navigation.AppQueryParam
import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.ui.sdui.UIState
import com.diegoferreiracaetano.dlearn.ui.viewmodel.ScreenLoader
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MovieDetailViewModel(
    private val movieId: String,
    private val repository: MovieDetailRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow<UIState<Screen>>(UIState.Loading)
    val uiState: StateFlow<UIState<Screen>> = _uiState.asStateFlow()

    private val loader = ScreenLoader(viewModelScope, _uiState)

    init {
        loader.load {
            repository.execute(
                AppRequest(
                    path = AppNavigationRoute.MOVIES,
                    params = mapOf(AppQueryParam.ID to movieId),
                ),
            )
        }
    }

    fun onAction(action: MovieDetailAction) {
        when (action) {
            is MovieDetailAction.Open -> {
                val request = AppPath.parse(action.url)
                loader.refresh { repository.execute(request) }
            }

            MovieDetailAction.Retry -> loader.retry()
        }
    }
}
