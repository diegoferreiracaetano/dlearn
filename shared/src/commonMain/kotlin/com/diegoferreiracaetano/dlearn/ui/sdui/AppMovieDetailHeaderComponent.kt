package com.diegoferreiracaetano.dlearn.ui.sdui

import kotlinx.serialization.Serializable

@Serializable
data class WatchProviderComponent(
    val name: String,
    val iconUrl: String,
    val priceInfo: String,
    val appUrl: String? = null,
    val webUrl: String? = null,
    val tmdbUrl: String? = null,
)

@Serializable
data class AppMovieDetailHeaderComponent(
    val title: String,
    val imageUrl: String,
    val year: String? = null,
    val duration: String? = null,
    val genre: String? = null,
    val rating: Double? = null,
    val trailerId: String? = null,
    val downloadActionUrl: String? = null,
    val shareActionUrl: String? = null,
    val favoriteActionUrl: String? = null,
    val watchlistActionUrl: String? = null,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
    val providers: List<WatchProviderComponent> = emptyList(),
) : Component
