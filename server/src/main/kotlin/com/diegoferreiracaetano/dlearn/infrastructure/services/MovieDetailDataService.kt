package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient

/**
 * Service responsible for fetching and processing movie/TV show details from TMDB.
 * Adheres to Single Responsibility Principle by delegating mapping to [TmdbMapper].
 */
class MovieDetailDataService(
    private val tmdbClient: TmdbClient,
    private val tmdbMapper: TmdbMapper
) {
    suspend fun fetchMovieDetail(movieId: String): MovieDetailDomainData {
        val response = try {
            tmdbClient.getMovieDetail(movieId)
        } catch (_: Exception) {
            // Fallback to TV show if movie is not found, or vice-versa
            tmdbClient.getTvShowDetail(movieId)
        }

        return tmdbMapper.toMovieDetail(response)
    }
}
