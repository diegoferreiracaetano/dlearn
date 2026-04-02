package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.selectAll

class MovieDetailDataService(
    private val movieClient: MovieClient,
) {
    suspend fun fetchMovieDetail(
        movieId: String,
        language: String,
        userId: String,
    ): MovieDetailDomainData =
        coroutineScope {
            // O MovieClient já resolve internamente se é Movie ou TV baseado no ID único
            val movieDetailDeferred =
                async {
                    movieClient.getMovieDetail(movieId, language)
                }

            // Para consulta no banco local, ainda precisamos do split para bater nas colunas mediaId (Int) e mediaType (String)
            val localStatesDeferred =
                async {
                    val parts = movieId.split("_")
                    val type =
                        if (parts.size == 2) {
                            runCatching { MediaType.valueOf(parts[0]) }.getOrElse { MediaType.MOVIES }
                        } else {
                            MediaType.MOVIES
                        }
                    val id = parts.last().toIntOrNull() ?: 0

                    dbQuery {
                        val isFavorite =
                            FavoriteTable
                                .selectAll()
                                .where {
                                    (FavoriteTable.userId eq userId) and
                                        (FavoriteTable.mediaId eq id) and
                                        (FavoriteTable.mediaType eq type.name)
                                }.any()

                        val isInWatchlist =
                            WatchlistTable
                                .selectAll()
                                .where {
                                    (WatchlistTable.userId eq userId) and
                                        (WatchlistTable.mediaId eq id) and
                                        (WatchlistTable.mediaType eq type.name)
                                }.any()

                        isFavorite to isInWatchlist
                    }
                }

            val movieDetail = movieDetailDeferred.await()
            val (localFavorite, localWatchlist) = localStatesDeferred.await()

            movieDetail.copy(
                isFavorite = localFavorite,
                isInWatchlist = localWatchlist,
            )
        }
}
