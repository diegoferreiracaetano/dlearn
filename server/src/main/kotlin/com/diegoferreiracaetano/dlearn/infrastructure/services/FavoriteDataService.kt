package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.FavoriteRepository
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class FavoriteDataService : FavoriteRepository {

    override suspend fun toggleFavorite(userId: String, mediaId: Int, mediaType: MediaType, isFavorite: Boolean) {
        dbQuery {
            if (isFavorite) {
                FavoriteTable.insertIgnore {
                    it[this.userId] = userId
                    it[this.mediaId] = mediaId
                    it[this.mediaType] = mediaType.name
                }
            } else {
                FavoriteTable.deleteWhere {
                    (FavoriteTable.userId eq userId) and 
                    (FavoriteTable.mediaId eq mediaId) and 
                    (FavoriteTable.mediaType eq mediaType.name)
                }
            }
        }
    }

    override suspend fun isFavorite(userId: String, mediaId: Int, mediaType: MediaType): Boolean = dbQuery {
        FavoriteTable.selectAll().where {
            (FavoriteTable.userId eq userId) and 
            (FavoriteTable.mediaId eq mediaId) and 
            (FavoriteTable.mediaType eq mediaType.name)
        }.count() > 0
    }

    override suspend fun getFavorites(userId: String): List<Pair<Int, MediaType>> = dbQuery {
        FavoriteTable.selectAll().where { FavoriteTable.userId eq userId }
            .map { it[FavoriteTable.mediaId] to MediaType.valueOf(it[FavoriteTable.mediaType]) }
    }
}
