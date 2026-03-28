package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient

/**
 * Service responsible for fetching and processing movie/TV show details from TMDB.
 * This service is the single source of truth for "Read" operations in the Detail domain.
 */
class MovieDetailDataService(
    private val tmdbClient: TmdbClient,
    private val tmdbMapper: TmdbMapper
) {
    suspend fun fetchMovieDetail(movieId: String, language: String, header: AppHeader): MovieDetailDomainData {
        val response = try {
            tmdbClient.getMovieDetail(movieId, language)
        } catch (_: Exception) {
            tmdbClient.getTvShowDetail(movieId, language)
        }

        val accountStates = header.tmdbSessionId?.let { sessionId ->
            try {
                tmdbClient.getAccountStates(movieId, sessionId)
            } catch (_: Exception) {
                null
            }
        }

        return tmdbMapper.toMovieDetail(response, accountStates)
    }
}
