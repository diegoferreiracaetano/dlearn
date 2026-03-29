package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.MetadataKeys.TMDB_ACCOUNT_ID
import com.diegoferreiracaetano.dlearn.MetadataKeys.TMDB_SESSION_ID
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteDataService(
    private val tmdbClient: TmdbClient,
    private val authProviderRepository: AuthProviderRepository
) : FavoriteRepository {

    override suspend fun markAsFavorite(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        favorite: Boolean
    ) {
        val providers = authProviderRepository.findByUserId(userId)
        val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
            ?: throw IllegalStateException("TMDB_NOT_LINKED")

        val sessionId = tmdbAccount.metadata[TMDB_SESSION_ID]
            ?: throw IllegalStateException("TMDB_SESSION_MISSING")
        val accountId = tmdbAccount.metadata[TMDB_ACCOUNT_ID]
            ?: throw IllegalStateException("TMDB_ACCOUNT_ID_MISSING")

        val response = runCatching {
            tmdbClient.markAsFavorite(
                accountId = accountId,
                sessionId = sessionId,
                mediaType = mediaType.name.lowercase(),
                mediaId = mediaId,
                favorite = favorite
            )
        }.getOrElse { throw IllegalStateException("TMDB_API_ERROR") }

        if (!response.success) {
            throw IllegalStateException("TMDB_ERROR_${response.statusCode}")
        }
    }

    override fun getFavorites(
        userId: String,
        language: String
    ): Flow<List<Pair<Int, MediaType>>> = flow {
        val providers = authProviderRepository.findByUserId(userId)
        val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
        
        if (tmdbAccount == null) {
            emit(emptyList())
            return@flow
        }

        val sessionId = tmdbAccount.metadata[TMDB_SESSION_ID]
        val accountId = tmdbAccount.metadata[TMDB_ACCOUNT_ID]

        if (sessionId == null || accountId == null) {
            emit(emptyList())
            return@flow
        }

        val result = try {
            coroutineScope {
                val moviesDeferred = async {
                    runCatching {
                        tmdbClient.getFavorite(accountId, sessionId, MediaType.MOVIES, language).results.map { it.id to MediaType.MOVIES }
                    }.getOrElse { emptyList() }
                }
                val seriesDeferred = async {
                    runCatching {
                        tmdbClient.getFavorite(accountId, sessionId, MediaType.SERIES, language).results.map { it.id to MediaType.SERIES }
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
