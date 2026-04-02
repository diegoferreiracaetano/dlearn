package com.diegoferreiracaetano.dlearn.infrastructure.db

import org.jetbrains.exposed.sql.Table

object AuthProviderTable : Table("auth_providers") {
    val id = integer("id").autoIncrement()
    val userId = varchar("user_id", 36).references(UserTable.id)
    val provider = varchar("provider", 50)
    val externalId = varchar("external_id", 255)
    val metadata = text("metadata").nullable()

    override val primaryKey = PrimaryKey(id)

    init {
        uniqueIndex("idx_user_provider_unique", userId, provider)
    }
}
