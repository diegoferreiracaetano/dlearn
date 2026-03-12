package com.diegoferreiracaetano.dlearn.domain.models

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
    val isInList: Boolean = false,
    val providers: List<WatchProviderDomainData> = emptyList()
)

data class WatchProviderDomainData(
    val name: String,
    val iconUrl: String,
    val priceInfo: String,
    val watchUrl: String? = null
)

data class CastMemberDomainData(
    val name: String,
    val role: String,
    val imageUrl: String?
)

data class SeasonDomainData(
    val number: Int,
    val episodeCount: Int
)
