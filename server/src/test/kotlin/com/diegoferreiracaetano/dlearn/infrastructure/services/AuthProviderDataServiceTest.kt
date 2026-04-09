package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.user.AccountProvider
import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider
import com.diegoferreiracaetano.dlearn.infrastructure.db.AuthProviderTable
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
import kotlin.test.assertTrue

class AuthProviderDataServiceTest {

    private lateinit var authProviderDataService: AuthProviderDataService
    private lateinit var db: Database
    private val dbName = "test_auth_provider_${UUID.randomUUID().toString().replace("-", "")}"

    @Before
    fun setup() {
        db = Database.connect("jdbc:h2:mem:$dbName;DB_CLOSE_DELAY=-1;MODE=MySQL", driver = "org.h2.Driver")
        transaction(db) {
            SchemaUtils.create(UserTable, AuthProviderTable)
        }
        authProviderDataService = AuthProviderDataService()
    }

    @After
    fun tearDown() {
        transaction(db) {
            SchemaUtils.drop(AuthProviderTable, UserTable)
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
    fun `given multiple auth providers when saveAll and findByUserId should persist and return all providers`() = runBlocking {
        createUser("user1")
        val providers = listOf(
            AuthProvider(AccountProvider.GOOGLE, "g123", mapOf("name" to "Google User")),
            AuthProvider(AccountProvider.APPLE, "a456", emptyMap())
        )
        authProviderDataService.saveAll("user1", providers)

        val found = authProviderDataService.findByUserId("user1")
        assertEquals(2, found.size)
        assertTrue(found.any { it.provider == AccountProvider.GOOGLE && it.externalId == "g123" })
        assertTrue(found.any { it.provider == AccountProvider.APPLE && it.externalId == "a456" })
        assertEquals("Google User", found.first { it.provider == AccountProvider.GOOGLE }.metadata["name"])
    }

    @Test
    fun `given existing auth providers when deleteByUserId should remove all providers for the user`() = runBlocking {
        createUser("user2")
        val providers = listOf(AuthProvider(AccountProvider.GOOGLE, "g123", emptyMap()))
        authProviderDataService.saveAll("user2", providers)

        authProviderDataService.deleteByUserId("user2")
        val found = authProviderDataService.findByUserId("user2")
        assertTrue(found.isEmpty())
    }

    @Test
    fun `given an existing auth provider when saveAll with updated data should update the record`() = runBlocking {
        createUser("user3")
        val initial = listOf(AuthProvider(AccountProvider.GOOGLE, "old", emptyMap()))
        authProviderDataService.saveAll("user3", initial)

        val updated = listOf(AuthProvider(AccountProvider.GOOGLE, "new", mapOf("v" to "2")))
        authProviderDataService.saveAll("user3", updated)

        val found = authProviderDataService.findByUserId("user3")
        assertEquals(1, found.size)
        assertEquals("new", found[0].externalId)
        assertEquals("2", found[0].metadata["v"])
    }
}
