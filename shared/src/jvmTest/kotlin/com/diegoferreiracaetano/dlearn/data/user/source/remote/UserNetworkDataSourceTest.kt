package com.diegoferreiracaetano.dlearn.data.user.source.remote

import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull
import kotlin.test.assertTrue

class UserNetworkDataSourceTest {

    private val dataSource = UserNetworkDataSource()

    @Test
    fun `when loadUser is called should return initial users`() = runTest {
        val users = dataSource.loadUser()
        assertTrue(users.isNotEmpty())
        assertEquals("Diego", users[0].name)
    }

    @Test
    fun `when saveUser is called should add new user to list`() = runTest {
        val newUser = UserRemote(id = "3", name = "Test", email = "test@test.com", password = "123")
        dataSource.saveUser(newUser)
        
        val users = dataSource.loadUser()
        assertTrue(users.contains(newUser))
    }

    @Test
    fun `when findByUser is called with valid credentials should return user`() = runTest {
        val user = dataSource.findByUser("diego@gmail.com", "teste")
        assertNotNull(user)
        assertEquals("Diego", user.name)
    }

    @Test
    fun `when findByUser is called with invalid credentials should return null`() = runTest {
        val user = dataSource.findByUser("diego@gmail.com", "wrong")
        assertNull(user)
    }

    @Test
    fun `when findByEmail is called with valid email should return user`() = runTest {
        val user = dataSource.findByEmail("lucas@gmail.com")
        assertNotNull(user)
        assertEquals("Lucas", user.name)
    }

    @Test
    fun `when findByEmail is called with invalid email should return null`() = runTest {
        val user = dataSource.findByEmail("nonexistent@gmail.com")
        assertNull(user)
    }
}
