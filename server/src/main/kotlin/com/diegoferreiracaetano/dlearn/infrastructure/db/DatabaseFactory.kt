package com.diegoferreiracaetano.dlearn.infrastructure.db

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init() {
        val driverClassName = "org.sqlite.JDBC"
        val jdbcUrl = "jdbc:sqlite:./dlearn.db"
        val database = Database.connect(jdbcUrl, driverClassName)

        transaction(database) {
            SchemaUtils.create(UserTable, AuthProviderTable, FavoriteTable, WatchlistTable)
        }
    }

    suspend fun <T> dbQuery(
        dispatcher: CoroutineDispatcher = Dispatchers.IO,
        block: suspend () -> T,
    ): T = newSuspendedTransaction(dispatcher) { block() }
}
