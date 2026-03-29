package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.db.UserTable
import kotlinx.coroutines.runBlocking
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserDataServiceTest {

    private lateinit var userDataService: UserDataService

    @Before
    fun setup() {
        Database.connect("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1;", driver = "org.h2.Driver")
        transaction {
            SchemaUtils.create(UserTable)
        }
        userDataService = UserDataService()
    }

    @Test
    fun `salvar e buscar por email deve funcionar corretamente`() = runBlocking {
        val user = User(id = "1", name = "Test", email = "test@test.com")
        userDataService.save(user, "password123")

        val found = userDataService.findByEmail("test@test.com")
        assertNotNull(found)
        assertEquals(user.name, found.name)
        assertEquals(user.email, found.email)
    }

    @Test
    fun `autenticar deve retornar usuario em caso de sucesso`() = runBlocking {
        val user = User(id = "2", name = "Auth", email = "auth@test.com")
        userDataService.save(user, "secret")

        val authenticated = userDataService.authenticate("auth@test.com", "secret")
        assertNotNull(authenticated)
        assertEquals("Auth", authenticated.name)
    }

    @Test
    fun `autenticar deve retornar null com senha errada`() = runBlocking {
        val user = User(id = "3", name = "Wrong", email = "wrong@test.com")
        userDataService.save(user, "right")

        val authenticated = userDataService.authenticate("wrong@test.com", "incorrect")
        assertNull(authenticated)
    }
}
