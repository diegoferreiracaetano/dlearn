package com.diegoferreiracaetano.dlearn.infrastructure.db

import org.jetbrains.exposed.sql.Table

object UserTable : Table("users") {
    val id = varchar("id", 36)
    val name = varchar("name", 128)
    val email = varchar("email", 128).uniqueIndex()
    val password = varchar("password", 128)
    val avatarUrl = varchar("avatar_url", 255).nullable()

    override val primaryKey = PrimaryKey(id)
}
