package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.CastMemberDomainData
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.models.WatchProviderDomainData
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import java.util.Locale

class MovieDetailDataService(private val tmdbClient: TmdbClient) {
    suspend fun fetchMovieDetail(movieId: String): MovieDetailDomainData {
        val response = try {
            tmdbClient.getMovieDetail(movieId)
        } catch (_: Exception) {
            tmdbClient.getTvShowDetail(movieId)
        }

        val trailerId = response.videos?.results
            ?.filter { it.site == "YouTube" && (it.type == "Trailer" || it.type == "Teaser") }
            ?.firstOrNull()?.key

        return MovieDetailDomainData(
            id = response.id.toString(),
            title = response.title ?: response.name ?: "",
            imageUrl = "https://image.tmdb.org/t/p/w500${response.posterPath}",
            year = (response.releaseDate ?: response.firstAirDate ?: "").take(4),
            duration = response.runtime?.let { "$it Minutes" } ?: "",
            genre = response.genres.firstOrNull()?.name ?: "",
            rating = String.format(Locale.US, "%.1f", response.voteAverage ?: 0.0),
            storyLine = response.overview ?: "",
            cast = response.credits?.cast?.take(10)?.map {
                CastMemberDomainData(
                    name = it.name,
                    role = it.character,
                    imageUrl = it.profilePath?.let { path -> "https://image.tmdb.org/t/p/w185$path" }
                )
            } ?: emptyList(),
            seasons = emptyList(),
            trailerId = trailerId,
            providers = response.watchProviders?.results?.get("BR")?.flatrate?.map {
                WatchProviderDomainData(
                    name = it.providerName ?: "",
                    iconUrl = "https://image.tmdb.org/t/p/w185${it.logoPath}",
                    priceInfo = "Assinar"
                )
            } ?: emptyList()
        )
    }
}
