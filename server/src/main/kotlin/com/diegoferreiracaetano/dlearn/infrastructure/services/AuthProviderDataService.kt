package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider
import com.diegoferreiracaetano.dlearn.infrastructure.db.AuthProviderTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq

class AuthProviderDataService : AuthProviderRepository {
    override suspend fun findByUserId(userId: String): List<AuthProvider> = dbQuery {
        AuthProviderTable.selectAll().where { AuthProviderTable.userId eq userId }
            .map { row ->
                AuthProvider(
                    provider = AccountProvider.valueOf(row[AuthProviderTable.provider]),
                    externalId = row[AuthProviderTable.externalId],
                    metadata = row[AuthProviderTable.metadata]?.let { Json.decodeFromString(it) } ?: emptyMap()
                )
            }
    }

    override suspend fun saveAll(userId: String, providers: List<AuthProvider>) {
        dbQuery {
            providers.forEach { provider ->
                AuthProviderTable.insert {
                    it[this.userId] = userId
                    it[this.provider] = provider.provider.name
                    it[this.externalId] = provider.externalId
                    it[this.metadata] = Json.encodeToString(provider.metadata)
                }
            }
        }
    }

    override suspend fun deleteByUserId(userId: String) {
        dbQuery {
            AuthProviderTable.deleteWhere { AuthProviderTable.userId eq userId }
        }
    }
}
