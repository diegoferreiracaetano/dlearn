package com.diegoferreiracaetano.dlearn.data.user.source.remote

import com.diegoferreiracaetano.dlearn.domain.user.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserRepositoryRemoteTest {

    private val dataSource = mockk<UserNetworkDataSource>(relaxed = true)
    private val repository = UserRepositoryRemote(dataSource)

    @Test
    fun `when users is called should return mapped users from datasource`() = runTest {
        val remoteUser = UserRemote(id = "1", name = "Test", email = "test@test.com", password = "123")
        coEvery { dataSource.loadUser() } returns listOf(remoteUser)

        val result = repository.users().first()

        assertEquals(1, result.size)
        assertEquals("Test", result[0].name)
    }

    @Test
    fun `when save is called should delegate to datasource`() = runTest {
        val user = User(id = "1", name = "Test", email = "test@test.com")
        repository.save(user)

        coVerify(exactly = 1) { dataSource.saveUser(any()) }
    }

    @Test
    fun `when findByUser is called should return mapped user`() = runTest {
        val remoteUser = UserRemote(id = "1", name = "Test", email = "test@test.com", password = "123")
        coEvery { dataSource.findByUser("test@test.com", "123") } returns remoteUser

        val result = repository.findByUser("test@test.com", "123").first()

        assertNotNull(result)
        assertEquals("Test", result.name)
    }

    @Test
    fun `when findByEmail is called should return mapped user`() = runTest {
        val remoteUser = UserRemote(id = "1", name = "Test", email = "test@test.com", password = "123")
        coEvery { dataSource.findByEmail("test@test.com") } returns remoteUser

        val result = repository.findByEmail("test@test.com").first()

        assertNotNull(result)
        assertEquals("Test", result.name)
    }

    @Test
    fun `when sendCode and getCode are called should handle internal state`() = runTest {
        repository.sendCode("test@test.com")
        val code = repository.getCode()
        assertEquals("test@test.com123456", code)
    }
}
