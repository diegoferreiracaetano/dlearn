package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.infrastructure.db.UserTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.WatchlistTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class WatchlistDataServiceTest {

    private lateinit var watchlistDataService: WatchlistDataService
    private lateinit var db: Database
    private val dbName = "test_watchlist_${UUID.randomUUID().toString().replace("-", "")}"

    @Before
    fun setup() {
        db = Database.connect("jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1;MODE=MySQL", driver = "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(UserTable, WatchlistTable)
        }
        watchlistDataService = WatchlistDataService()
    }

    @After
    fun tearDown() {
        transaction(db) {
            SchemaUtils.drop(WatchlistTable, UserTable)
        }
    }

    private fun createUser(userId: String) {
        transaction(db) {
            UserTable.insert {
                it[id] = userId
                it[name] = "Test User"
                it[email] = "$userId@test.com"
                it[password] = "pass"
            }
        }
    }

    @Test
    fun `given an item not in watchlist when toggleWatchlist with true is called should add the item to the watchlist`() = runBlocking {
        createUser("user1")
        watchlistDataService.toggleWatchlist("user1", "MOVIES_123", true)

        val isIn = watchlistDataService.isInWatchlist("user1", "MOVIES_123")
        assertTrue(isIn)
    }

    @Test
    fun `given an item in watchlist when toggleWatchlist with false is called should remove the item from the watchlist`() = runBlocking {
        createUser("user1")
        watchlistDataService.toggleWatchlist("user1", "MOVIES_123", true)
        watchlistDataService.toggleWatchlist("user1", "MOVIES_123", false)

        val isIn = watchlistDataService.isInWatchlist("user1", "MOVIES_123")
        assertFalse(isIn)
    }

    @Test
    fun `given multiple items in watchlist when getWatchlist is called should return all formatted IDs`() = runBlocking {
        createUser("user2")
        watchlistDataService.toggleWatchlist("user2", "SERIES_456", true)
        watchlistDataService.toggleWatchlist("user2", "MOVIES_789", true)

        val list = watchlistDataService.getWatchlist("user2")
        assertEquals(2, list.size)
        assertTrue(list.contains("SERIES_456"))
        assertTrue(list.contains("MOVIES_789"))
    }

    @Test
    fun `given an ID without prefix when toggleWatchlist is called should assume MOVIES as default media type`() = runBlocking {
        createUser("user3")
        watchlistDataService.toggleWatchlist("user3", "123", true)
        assertTrue(watchlistDataService.isInWatchlist("user3", "MOVIES_123"))
    }
}
