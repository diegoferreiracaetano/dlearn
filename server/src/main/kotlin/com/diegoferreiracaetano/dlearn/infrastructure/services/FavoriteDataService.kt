package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll

class FavoriteDataService : FavoriteRepository {
    private fun parseMovieId(movieId: String): Pair<Int, MediaType> {
        val parts = movieId.split("_")
        val mediaType =
            if (parts.size == 2) {
                runCatching { MediaType.valueOf(parts[0]) }.getOrElse { MediaType.MOVIES }
            } else {
                MediaType.MOVIES
            }
        val tmdbId = parts.last().toIntOrNull() ?: 0
        return tmdbId to mediaType
    }

    override suspend fun toggleFavorite(
        userId: String,
        mediaId: String,
        isFavorite: Boolean,
    ) {
        val (id, type) = parseMovieId(mediaId)
        dbQuery {
            if (isFavorite) {
                FavoriteTable.insertIgnore {
                    it[this.userId] = userId
                    it[this.mediaId] = id
                    it[this.mediaType] = type.name
                }
            } else {
                FavoriteTable.deleteWhere {
                    (FavoriteTable.userId eq userId) and
                        (FavoriteTable.mediaId eq id) and
                        (FavoriteTable.mediaType eq type.name)
                }
            }
        }
    }

    override suspend fun isFavorite(
        userId: String,
        mediaId: String,
    ): Boolean {
        val (id, type) = parseMovieId(mediaId)
        return dbQuery {
            FavoriteTable
                .selectAll()
                .where {
                    (FavoriteTable.userId eq userId) and
                        (FavoriteTable.mediaId eq id) and
                        (FavoriteTable.mediaType eq type.name)
                }.count() > 0
        }
    }

    override suspend fun getFavorites(userId: String): List<String> =
        dbQuery {
            FavoriteTable
                .selectAll()
                .where { FavoriteTable.userId eq userId }
                .map { "${it[FavoriteTable.mediaType]}_${it[FavoriteTable.mediaId]}" }
        }
}
