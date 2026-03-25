package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.UserTable
import org.jetbrains.exposed.sql.*

class UserDataService : UserRepository {
    override suspend fun findByEmail(email: String): User? = dbQuery {
        val normalizedEmail = email.trim().lowercase()
        UserTable.selectAll().where { UserTable.email eq normalizedEmail }
            .map(::toUser)
            .singleOrNull()
    }

    override suspend fun findById(id: String): User? = dbQuery {
        UserTable.selectAll().where { UserTable.id eq id }
            .map(::toUser)
            .singleOrNull()
    }

    override suspend fun findAll(): List<User> = dbQuery {
        UserTable.selectAll()
            .map(::toUser)
    }

    override suspend fun save(user: User): User = dbQuery {
        val exists = UserTable.selectAll().where { UserTable.id eq user.id }.any()
        val normalizedEmail = user.email.trim().lowercase()
        
        if (exists) {
            UserTable.update({ UserTable.id eq user.id }) {
                it[name] = user.name.trim()
                it[email] = normalizedEmail
                it[password] = user.password ?: ""
                it[avatarUrl] = user.imageUrl
            }
        } else {
            UserTable.insert {
                it[id] = user.id
                it[name] = user.name.trim()
                it[email] = normalizedEmail
                it[password] = user.password ?: ""
                it[avatarUrl] = user.imageUrl
            }
        }

        UserTable.selectAll().where { UserTable.id eq user.id }
            .map(::toUser)
            .single()
    }

    private fun toUser(row: ResultRow) = User(
        id = row[UserTable.id],
        name = row[UserTable.name],
        email = row[UserTable.email],
        password = row[UserTable.password],
        imageUrl = row[UserTable.avatarUrl],
        isPremium = false
    )
}
