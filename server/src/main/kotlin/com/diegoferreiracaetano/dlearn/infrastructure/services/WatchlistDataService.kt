package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class WatchlistDataService(
    private val tmdbClient: TmdbClient,
    private val authProviderRepository: AuthProviderRepository
) : WatchlistRepository {

    override suspend fun addToWatchlist(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        watchlist: Boolean
    ) {
        val providers = authProviderRepository.findByUserId(userId)
        val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
            ?: throw IllegalStateException("TMDB_NOT_LINKED")

        val sessionId = tmdbAccount.metadata[MetadataKeys.TMDB_SESSION_ID] 
            ?: throw IllegalStateException("TMDB_SESSION_MISSING")
        val accountId = tmdbAccount.metadata[MetadataKeys.TMDB_ACCOUNT_ID] 
            ?: throw IllegalStateException("TMDB_ACCOUNT_ID_MISSING")

        val response = runCatching {
            tmdbClient.addToWatchlist(
                accountId = accountId,
                sessionId = sessionId,
                mediaType = mediaType.name.lowercase(),
                mediaId = mediaId,
                watchlist = watchlist
            )
        }.getOrElse { throw IllegalStateException("TMDB_API_ERROR") }

        if (!response.success) {
            throw IllegalStateException("TMDB_ERROR_${response.statusCode}")
        }
    }

    override fun getWatchlist(
        userId: String,
        language: String
    ): Flow<List<Pair<Int, MediaType>>> = flow {
        val providers = authProviderRepository.findByUserId(userId)
        val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
        
        if (tmdbAccount == null) {
            emit(emptyList())
            return@flow
        }

        val sessionId = tmdbAccount.metadata[MetadataKeys.TMDB_SESSION_ID]
        val accountId = tmdbAccount.metadata[MetadataKeys.TMDB_ACCOUNT_ID]

        if (sessionId == null || accountId == null) {
            emit(emptyList())
            return@flow
        }

        val result = try {
            coroutineScope {
                val moviesDeferred = async {
                    runCatching {
                        tmdbClient.getWatchlist(accountId, sessionId, MediaType.MOVIES, language).results.map { it.id to MediaType.MOVIES }
                    }.getOrElse { emptyList() }
                }
                val seriesDeferred = async {
                    runCatching {
                        tmdbClient.getWatchlist(accountId, sessionId,  MediaType.SERIES, language).results.map { it.id to MediaType.SERIES }
                    }.getOrElse { emptyList() }
                }

                moviesDeferred.await() + seriesDeferred.await()
            }
        } catch (e: Exception) {
            emptyList()
        }

        emit(result)
    }
}
