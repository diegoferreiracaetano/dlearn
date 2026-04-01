package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.WatchlistRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insertIgnore
import org.jetbrains.exposed.sql.selectAll

class WatchlistDataService : WatchlistRepository {

    private fun parseMovieId(movieId: String): Pair<Int, MediaType> {
        val parts = movieId.split("_")
        val mediaType = if (parts.size == 2) {
            runCatching { MediaType.valueOf(parts[0]) }.getOrElse { MediaType.MOVIES }
        } else {
            MediaType.MOVIES
        }
        val tmdbId = parts.last().toIntOrNull() ?: 0
        return tmdbId to mediaType
    }

    override suspend fun toggleWatchlist(
        userId: String,
        mediaId: String,
        isInWatchlist: Boolean
    ) {
        val (id, type) = parseMovieId(mediaId)
        dbQuery {
            if (isInWatchlist) {
                WatchlistTable.insertIgnore {
                    it[WatchlistTable.userId] = userId
                    it[WatchlistTable.mediaId] = id
                    it[WatchlistTable.mediaType] = type.name
                }
            } else {
                WatchlistTable.deleteWhere {
                    (WatchlistTable.userId eq userId) and 
                    (WatchlistTable.mediaId eq id) and 
                    (WatchlistTable.mediaType eq type.name)
                }
            }
        }
    }

    override suspend fun isInWatchlist(userId: String, mediaId: String): Boolean {
        val (id, type) = parseMovieId(mediaId)
        return dbQuery {
            WatchlistTable.selectAll().where {
                (WatchlistTable.userId eq userId) and 
                (WatchlistTable.mediaId eq id) and 
                (WatchlistTable.mediaType eq type.name)
            }.count() > 0
        }
    }

    override suspend fun getWatchlist(userId: String): List<String> = dbQuery {
        WatchlistTable.selectAll()
            .where { WatchlistTable.userId eq userId }
            .map { 
                "${it[WatchlistTable.mediaType]}_${it[WatchlistTable.mediaId]}"
            }
    }
}
