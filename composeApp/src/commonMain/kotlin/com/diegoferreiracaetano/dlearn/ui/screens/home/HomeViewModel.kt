package com.diegoferreiracaetano.dlearn.ui.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.diegoferreiracaetano.dlearn.data.util.OrderType
import com.diegoferreiracaetano.dlearn.domain.video.VideoRepository
import com.diegoferreiracaetano.dlearn.domain.video.VideoType
import com.diegoferreiracaetano.dlearn.ui.screens.home.state.HomeUiState
import com.diegoferreiracaetano.dlearn.util.filterNotNull
import com.diegoferreiracaetano.dlearn.util.filterOrNull
import com.diegoferreiracaetano.dlearn.util.isPositive
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(
    private val repository: VideoRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState = _uiState.asStateFlow()

    init {
        fetchVideos()
    }

    private fun fetchVideos() {
        viewModelScope.launch {
            repository.list(search = "", category = "", order = OrderType.ASC)
                .catch { e ->
                    _uiState.value = HomeUiState.Error(e.message)
                }
                .collect { videos ->
                    val banners = videos.filterOrNull { it.type == VideoType.BANNER }

                    val continueWatching = videos.filterOrNull { it.progress.isPositive() }

                    val favorites = videos.filterOrNull { it.isFavorite }

                    val carousels = videos.filterNot { it.type != VideoType.DEFAULT }
                        .groupBy { video -> video.categories.first() }.map { (category, videos) ->
                        HomeUiState.Success.CarouselUiState(
                            title = category.name.lowercase().replaceFirstChar { it.uppercase() },
                            items = videos
                        )
                    }

                    _uiState.value = HomeUiState.Success(
                        banners = banners,
                        favorites = favorites,
                        continueWatching = continueWatching,
                        carousels = carousels
                    )
                }
        }
    }
}
