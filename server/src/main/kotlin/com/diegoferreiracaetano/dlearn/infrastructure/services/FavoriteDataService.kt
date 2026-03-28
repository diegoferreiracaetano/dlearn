package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient

class FavoriteDataService(
    private val tmdbClient: TmdbClient
) : FavoriteRepository {

    override suspend fun markAsFavorite(
        accountId: String,
        sessionId: String,
        mediaId: Int,
        favorite: Boolean
    ): Boolean {
        return try {
            tmdbClient.markAsFavorite(
                accountId = accountId,
                sessionId = sessionId,
                mediaType = "movie",
                mediaId = mediaId,
                favorite = favorite
            )
            true
        } catch (e: Exception) {
            false
        }
    }

    override suspend fun getFavorites(accountId: String, sessionId: String, language: String): List<Int> {
        return try {
            tmdbClient.getFavoriteMovies(accountId, sessionId, language).results.map { it.id }
        } catch (e: Exception) {
            emptyList()
        }
    }
}
