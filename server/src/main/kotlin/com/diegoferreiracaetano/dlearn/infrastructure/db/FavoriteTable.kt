package com.diegoferreiracaetano.dlearn.infrastructure.db

import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants
import org.jetbrains.exposed.sql.Table

object FavoriteTable : Table("favorites") {
    val id = integer("id").autoIncrement()
    val userId = varchar("user_id", ServerConstants.Database.UUID_LENGTH).references(UserTable.id)
    val mediaId = integer("media_id")
    val mediaType = varchar("media_type", ServerConstants.Database.MEDIA_TYPE_LENGTH)

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex("idx_user_favorite_unique", userId, mediaId, mediaType)
    }
}

object WatchlistTable : Table("watchlist") {
    val id = integer("id").autoIncrement()
    val userId = varchar("user_id", ServerConstants.Database.UUID_LENGTH).references(UserTable.id)
    val mediaId = integer("media_id")
    val mediaType = varchar("media_type", ServerConstants.Database.MEDIA_TYPE_LENGTH)

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex("idx_user_watchlist_unique", userId, mediaId, mediaType)
    }
}
