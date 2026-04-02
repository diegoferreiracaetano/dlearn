package com.diegoferreiracaetano.dlearn.infrastructure.db

import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants
import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id = varchar("id", ServerConstants.Database.UUID_LENGTH)
    val name = varchar("name", ServerConstants.Database.DEFAULT_VARCHAR_LENGTH)
    val email = varchar("email", ServerConstants.Database.DEFAULT_VARCHAR_LENGTH).uniqueIndex()
    val password = varchar("password", ServerConstants.Database.DEFAULT_VARCHAR_LENGTH)
    val avatarUrl = varchar("avatar_url", ServerConstants.Database.URL_LENGTH).nullable()
    val isPremium = bool("is_premium").default(false)

    override val primaryKey = PrimaryKey(id)
}
