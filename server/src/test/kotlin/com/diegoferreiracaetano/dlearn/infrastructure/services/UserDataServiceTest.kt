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
import kotlin.test.assertTrue

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

    @Test
    fun `given a saved user when findById should return the user`() = runBlocking {
        val user = User(id = "4", name = "ById", email = "byid@test.com")
        userDataService.save(user, "pass")

        val found = userDataService.findById("4")
        assertNotNull(found)
        assertEquals("ById", found.name)
    }

    @Test
    fun `given a non-existent id when findById should return null`() = runBlocking {
        val found = userDataService.findById("ghost-id")
        assertNull(found)
    }

    @Test
    fun `given multiple saved users when findAll should return all users`() = runBlocking {
        userDataService.save(User(id = "5", name = "User5", email = "u5@test.com"), "p")
        userDataService.save(User(id = "6", name = "User6", email = "u6@test.com"), "p")

        val all = userDataService.findAll()
        assertTrue(all.size >= 2)
        assertTrue(all.any { it.id == "5" })
        assertTrue(all.any { it.id == "6" })
    }

    @Test
    fun `given a saved user when update is called should persist the new values`() = runBlocking {
        val original = User(id = "7", name = "Original", email = "original@test.com")
        userDataService.save(original, "pass")

        val updated = original.copy(name = "Updated")
        userDataService.update(updated)

        val found = userDataService.findById("7")
        assertNotNull(found)
        assertEquals("Updated", found.name)
    }

    @Test
    fun `given a saved user when update is called with new password should authenticate with new password`() = runBlocking {
        val user = User(id = "8", name = "PassChange", email = "passchange@test.com")
        userDataService.save(user, "old-pass")

        userDataService.update(user, "new-pass")

        val auth = userDataService.authenticate("passchange@test.com", "new-pass")
        assertEquals("PassChange", auth?.name)
    }

    @Test
    fun `given email with mixed case when findByEmail should find via normalized email`() = runBlocking {
        val user = User(id = "9", name = "CaseTest", email = "case@test.com")
        userDataService.save(user, "pass")

        val found = userDataService.findByEmail("CASE@TEST.COM")
        assertNotNull(found)
        assertEquals("CaseTest", found.name)
    }
}
