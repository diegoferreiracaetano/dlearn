package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
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

        val tmdbMovie = tmdbMovieDeferred.await()
        val (localFavorite, localWatchlist) = localStatesDeferred.await()

        // 2. Usamos o estado local para Favoritos/Watchlist
        tmdbMapper.toMovieDetail(tmdbMovie, localFavorite, localWatchlist)
    }
}
