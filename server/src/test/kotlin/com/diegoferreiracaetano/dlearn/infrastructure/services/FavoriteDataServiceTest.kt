package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.infrastructure.db.FavoriteTable
import com.diegoferreiracaetano.dlearn.infrastructure.db.UserTable
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

class FavoriteDataServiceTest {

    private lateinit var favoriteDataService: FavoriteDataService
    private lateinit var db: Database
    private val dbName = "test_favorite_${UUID.randomUUID().toString().replace("-", "")}"

    @Before
    fun setup() {
        db = Database.connect("jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1;MODE=MySQL", driver = "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(UserTable, FavoriteTable)
        }
        favoriteDataService = FavoriteDataService()
    }

    @After
    fun tearDown() {
        transaction(db) {
            SchemaUtils.drop(FavoriteTable, UserTable)
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
    fun `given an item not in favorites when toggleFavorite with true should add the item to favorites`() = runBlocking {
        createUser("user1")
        favoriteDataService.toggleFavorite("user1", "MOVIES_123", true)
        val isFav = favoriteDataService.isFavorite("user1", "MOVIES_123")
        assertTrue(isFav)
    }

    @Test
    fun `given an item in favorites when toggleFavorite with false should remove the item from favorites`() = runBlocking {
        createUser("user1")
        favoriteDataService.toggleFavorite("user1", "MOVIES_123", true)
        favoriteDataService.toggleFavorite("user1", "MOVIES_123", false)
        val isFav = favoriteDataService.isFavorite("user1", "MOVIES_123")
        assertFalse(isFav)
    }

    @Test
    fun `given multiple favorite items when getFavorites should return all favorites for the user`() = runBlocking {
        createUser("user2")
        favoriteDataService.toggleFavorite("user2", "SERIES_123", true)
        favoriteDataService.toggleFavorite("user2", "MOVIES_456", true)
        
        val list = favoriteDataService.getFavorites("user2")
        assertEquals(2, list.size)
        assertTrue(list.contains("SERIES_123"))
        assertTrue(list.contains("MOVIES_456"))
    }
}
