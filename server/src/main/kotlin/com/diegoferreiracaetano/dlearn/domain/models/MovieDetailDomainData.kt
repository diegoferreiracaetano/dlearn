package com.diegoferreiracaetano.dlearn.domain.models

import com.diegoferreiracaetano.dlearn.domain.video.MediaType

data class MovieDetailDomainData(
    val id: String,
    val title: String,
    val imageUrl: String,
    val year: String,
    val duration: String,
    val genre: String,
    val rating: String,
    val storyLine: String,
    val cast: List<CastMemberDomainData>,
    val seasons: List<SeasonDomainData>,
    val trailerId: String? = null,
    val isFavorite: Boolean = false,
    val isInWatchlist: Boolean = false,
    val providers: List<WatchProviderDomainData> = emptyList(),
    val mediaType: MediaType = MediaType.MOVIES
)

data class CastMemberDomainData(
    val name: String,
    val role: String,
    val imageUrl: String?
)

data class WatchProviderDomainData(
    val id: Int?,
    val name: String,
    val iconUrl: String,
    val priceInfo: String,
    val appUrl: String? = null,
    val webUrl: String? = null,
    val tmdbUrl: String? = null
)

data class SeasonDomainData(
    val number: Int,
    val episodeCount: Int
)
