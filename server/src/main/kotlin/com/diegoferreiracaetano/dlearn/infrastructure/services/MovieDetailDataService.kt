package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class MovieDetailDataService(
    private val tmdbClient: TmdbClient,
    private val authProviderRepository: AuthProviderRepository,
    private val tmdbMapper: TmdbMapper
) {
    suspend fun fetchMovieDetail(
        movieId: String,
        language: String,
        header: AppHeader
    ): MovieDetailDomainData = coroutineScope {
        val userId = header.userId

        val tmdbMovieDeferred = async {
            runCatching {
                tmdbClient.getMovieDetail(movieId, language)
            }.getOrElse {
                tmdbClient.getTvShowDetail(movieId, language)
            }
        }

        val sessionId = if (userId != AppConstants.GUEST_USER_ID) {
            val providers = authProviderRepository.findByUserId(userId)
            val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
            tmdbAccount?.metadata?.get(MetadataKeys.TMDB_SESSION_ID)
        } else null

        val accountStatesDeferred = async {
            sessionId?.let { sid ->
                runCatching {
                    tmdbClient.getAccountStates(movieId, sid)
                }.getOrNull()
            }
        }

        val tmdbMovie = tmdbMovieDeferred.await()
        val accountStates = accountStatesDeferred.await()

        tmdbMapper.toMovieDetail(tmdbMovie, accountStates)
    }
}
