package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.db.UserTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserDataServiceTest {

    private lateinit var userDataService: UserDataService
    private lateinit var db: Database
    private val dbName = "test_user_${UUID.randomUUID().toString().replace("-", "")}"

    @Before
    fun setup() {
        db = Database.connect("jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(UserTable)
        }
        userDataService = UserDataService()
    }
    
    @After
    fun tearDown() {
        transaction(db) {
            SchemaUtils.drop(UserTable)
        }
    }

    @Test
    fun `given a user saved when findByEmail should return the user`() = runBlocking {
        val user = User(id = "1", name = "Test", email = "test@test.com")
        userDataService.save(user, "password123")

        val found = userDataService.findByEmail("test@test.com")
        assertNotNull(found)
        assertEquals(user.name, found.name)
        assertEquals(user.email, found.email)
    }

    @Test
    fun `given a valid email and password when authenticate should return the user`() = runBlocking {
        val user = User(id = "2", name = "Auth", email = "auth@test.com")
        userDataService.save(user, "secret")

        val authenticated = userDataService.authenticate("auth@test.com", "secret")
        assertNotNull(authenticated)
        assertEquals("Auth", authenticated.name)
    }

    @Test
    fun `given a valid email but wrong password when authenticate should return null`() = runBlocking {
        val user = User(id = "3", name = "Wrong", email = "wrong@test.com")
        userDataService.save(user, "right")

        val authenticated = userDataService.authenticate("wrong@test.com", "incorrect")
        assertNull(authenticated)
    }
}
