package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import com.diegoferreiracaetano.dlearn.infrastructure.db.UserTable
import com.diegoferreiracaetano.dlearn.infrastructure.model.UserRemote
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.update

class UserDataService : UserRepository {

    override suspend fun findByEmail(email: String): User? {
        val normalizedEmail = email.trim().lowercase()
        
        val remote = dbQuery {
            UserTable.selectAll().where { UserTable.email eq normalizedEmail }
                .map { it.toUserRemote() }
                .singleOrNull()
        }

        return remote?.toDomain()
    }

    override suspend fun findById(id: String): User? {
        val remote = dbQuery {
            UserTable.selectAll().where { UserTable.id eq id }
                .map { it.toUserRemote() }
                .singleOrNull()
        }

        return remote?.toDomain()
    }

    override suspend fun findAll(): List<User> = dbQuery {
        UserTable.selectAll()
            .map { it.toUserRemote().toDomain() }
    }

    override suspend fun save(user: User, password: String): User {
        dbQuery {
            UserTable.insert {
                it[id] = user.id
                it[name] = user.name.trim()
                it[email] = user.email.trim().lowercase()
                it[this.password] = password
                it[avatarUrl] = user.imageUrl
                it[isPremium] = user.isPremium
            }
        }
        return user
    }

    override suspend fun update(user: User, password: String?): User {
        dbQuery {
            UserTable.update({ UserTable.id eq user.id }) {
                it[name] = user.name.trim()
                it[email] = user.email.trim().lowercase()
                if (password != null) {
                    it[this.password] = password
                }
                it[avatarUrl] = user.imageUrl
                it[isPremium] = user.isPremium
            }
        }
        return user
    }

    override suspend fun authenticate(email: String, password: String): User? {
        val normalizedEmail = email.trim().lowercase()
        val remote = dbQuery {
            UserTable.selectAll().where { UserTable.email eq normalizedEmail }
                .map { it.toUserRemote() }
                .singleOrNull()
        }

        return if (remote?.password == password) {
            remote.toDomain()
        } else {
            null
        }
    }

    private fun ResultRow.toUserRemote(): UserRemote = UserRemote(
        id = this[UserTable.id],
        name = this[UserTable.name],
        email = this[UserTable.email],
        password = this[UserTable.password],
        imageUrl = this[UserTable.avatarUrl],
        isPremium = this[UserTable.isPremium]
    )

    private fun UserRemote.toDomain(): User = User(
        id = id,
        name = name,
        email = email,
        imageUrl = imageUrl,
        isPremium = isPremium,
        phoneNumber = phoneNumber
    )
}
