package com.diegoferreiracaetano.dlearn.infrastructure.db

import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants
import org.jetbrains.exposed.sql.Table

object AuthProviderTable : Table("auth_providers") {
    val id = integer("id").autoIncrement()
    val userId = varchar("user_id", ServerConstants.Database.UUID_LENGTH).references(UserTable.id)
    val provider = varchar("provider", ServerConstants.Database.PROVIDER_LENGTH)
    val externalId = varchar("external_id", ServerConstants.Database.URL_LENGTH)
    val metadata = text("metadata").nullable()

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex("idx_user_provider_unique", userId, provider)
    }
}
