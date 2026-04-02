package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.AuthProviderRepository
import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider
import com.diegoferreiracaetano.dlearn.infrastructure.db.AuthProviderTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.DatabaseFactory.dbQuery
import kotlinx.serialization.json.Json
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.slf4j.LoggerFactory

class AuthProviderDataService : AuthProviderRepository {
    private val logger = LoggerFactory.getLogger(javaClass)

    override suspend fun findByUserId(userId: String): List<AuthProvider> =
        dbQuery {
            AuthProviderTable
                .selectAll()
                .where { AuthProviderTable.userId eq userId }
                .map { row ->
                    AuthProvider(
                        provider = AccountProvider.valueOf(row[AuthProviderTable.provider]),
                        externalId = row[AuthProviderTable.externalId],
                        metadata = row[AuthProviderTable.metadata]?.let { Json.decodeFromString(it) }
                            ?: emptyMap(),
                    )
                }
        }

    override suspend fun saveAll(
        userId: String,
        providers: List<AuthProvider>,
    ) {
        dbQuery {
            providers.forEach { provider ->
                try {
                    AuthProviderTable.deleteWhere {
                        (AuthProviderTable.userId eq userId) and (AuthProviderTable.provider eq provider.provider.name)
                    }

                    val id =
                        AuthProviderTable.insert {
                            it[this.userId] = userId
                            it[this.provider] = provider.provider.name
                            it[this.externalId] = provider.externalId
                            it[this.metadata] = Json.encodeToString(provider.metadata)
                        } get AuthProviderTable.id

                    logger.info("Successfully persisted AuthProvider ${provider.provider} (ID: $id) for user $userId")
                } catch (e: Exception) {
                    logger.error(
                        "Failed to insert AuthProvider ${provider.provider} for user $userId",
                        e
                    )
                    throw e
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
