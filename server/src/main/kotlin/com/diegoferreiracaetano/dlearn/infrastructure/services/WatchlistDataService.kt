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

    override suspend fun toggleWatchlist(
        userId: String,
        mediaId: Int,
        mediaType: MediaType,
        isWatchlist: Boolean
    ) {
        // Agora persiste apenas no banco local (Source of Truth)
        dbQuery {
            if (isWatchlist) {
                WatchlistTable.insertIgnore {
                    it[WatchlistTable.userId] = userId
                    it[WatchlistTable.mediaId] = mediaId
                    it[WatchlistTable.mediaType] = mediaType.name
                }
            } else {
                WatchlistTable.deleteWhere {
                    (WatchlistTable.userId eq userId) and 
                    (WatchlistTable.mediaId eq mediaId) and 
                    (WatchlistTable.mediaType eq mediaType.name)
                }
            }
        }
    }

    override suspend fun isWatchlist(userId: String, mediaId: Int, mediaType: MediaType): Boolean = dbQuery {
        WatchlistTable.selectAll().where {
            (WatchlistTable.userId eq userId) and 
            (WatchlistTable.mediaId eq mediaId) and 
            (WatchlistTable.mediaType eq mediaType.name)
        }.count() > 0
    }

    override suspend fun getWatchlist(userId: String): List<Pair<Int, MediaType>> = dbQuery {
        WatchlistTable.selectAll()
            .where { WatchlistTable.userId eq userId }
            .map { 
                it[WatchlistTable.mediaId] to MediaType.valueOf(it[WatchlistTable.mediaType]) 
            }
    }
}
