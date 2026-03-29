package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.AppConstants
import com.diegoferreiracaetano.dlearn.MetadataKeys
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.network.AppHeader
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll

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
        val mediaIdInt = movieId.toIntOrNull() ?: 0

        val tmdbMovieDeferred = async {
            runCatching {
                tmdbClient.getMovieDetail(movieId, language)
            }.getOrElse {
                tmdbClient.getTvShowDetail(movieId, language)
            }
        }

        var isGuestSession = false
        val sessionId = if (userId != AppConstants.GUEST_USER_ID) {
            val providers = authProviderRepository.findByUserId(userId)
            val tmdbAccount = providers.find { it.provider == AccountProvider.TMDB }
            isGuestSession = tmdbAccount?.metadata?.get("auth_type") == "guest_session"
            tmdbAccount?.metadata?.get(MetadataKeys.TMDB_SESSION_ID)
        } else null

        // 1. Buscamos o estado local sempre (Source of Truth)
        val localStatesDeferred = async {
            dbQuery {
                val isFavorite = FavoriteTable.selectAll().where {
                    (FavoriteTable.userId eq userId) and (FavoriteTable.mediaId eq mediaIdInt)
                }.any()

                val isInWatchlist = WatchlistTable.selectAll().where {
                    (WatchlistTable.userId eq userId) and (WatchlistTable.mediaId eq mediaIdInt)
                }.any()
                
                isFavorite to isInWatchlist
            }
        }

        // 2. Buscamos do TMDB se houver sessão (para ratings principalmente)
        val accountStatesDeferred = async {
            sessionId?.let { sid ->
                runCatching {
                    tmdbClient.getAccountStates(movieId, sid, isGuestSession)
                }.getOrNull()
            }
        }

        val tmdbMovie = tmdbMovieDeferred.await()
        val accountStates = accountStatesDeferred.await()
        val (localFavorite, localWatchlist) = localStatesDeferred.await()

        // 3. Mesclamos preferindo o estado local para Favoritos/Watchlist
        val finalAccountStates = (accountStates ?: tmdbMapper.createEmptyAccountStates(localFavorite, localWatchlist)).copy(
            favorite = localFavorite,
            watchlist = localWatchlist
        )

        tmdbMapper.toMovieDetail(tmdbMovie, finalAccountStates)
    }
}
