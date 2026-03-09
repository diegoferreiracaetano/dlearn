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
    val seasons: List<SeasonDomainData>
)

data class CastMemberDomainData(
    val id: String,
    val name: String,
    val role: String,
    val imageUrl: String?
)

data class SeasonDomainData(
    val number: Int,
    val episodes: List<EpisodeDomainData>
)

data class EpisodeDomainData(
    val id: String,
    val title: String,
    val duration: String,
    val description: String,
    val imageUrl: String,
    val isPremium: Boolean
)
