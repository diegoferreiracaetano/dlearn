package com.diegoferreiracaetano.dlearn.ui.screens.home.state

import com.diegoferreiracaetano.dlearn.domain.video.Video

sealed class HomeUiState {
    data object Loading : HomeUiState()
    data class Error(val message: String?) : HomeUiState()
    data class Success(
        val banners: List<Video>?,
        val favorites: List<Video>?,
        val continueWatching: List<Video>?,
        val carousels: List<CarouselUiState>?
    ) : HomeUiState() {
        data class CarouselUiState(
            val title: String,
            val items: List<Video>
        )
    }
}
