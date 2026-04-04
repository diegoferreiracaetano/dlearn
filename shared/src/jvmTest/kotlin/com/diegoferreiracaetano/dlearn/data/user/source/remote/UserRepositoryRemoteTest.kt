package com.diegoferreiracaetano.dlearn.data.user.source.remote

import com.diegoferreiracaetano.dlearn.domain.user.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class UserRepositoryRemoteTest {

    private val dataSource: UserNetworkDataSource = mockk()
    private lateinit var repository: UserRepositoryRemote

    private val userRemote = UserRemote(
        id = "1",
        name = "Test User",
        email = "test@example.com",
        password = "password"
    )

    private val user = User(
        id = "1",
        name = "Test User",
        email = "test@example.com"
    )

    @Before
    fun setup() {
        repository = UserRepositoryRemote(dataSource)
    }

    @Test
    fun `users should return list of users`() = runTest {
        coEvery { dataSource.loadUser() } returns listOf(userRemote)

        val result = repository.users().first()

        assertEquals(1, result.size)
        assertEquals(user.id, result[0].id)
        assertEquals(user.email, result[0].email)
    }

    @Test
    fun `save should call dataSource saveUser`() = runTest {
        coEvery { dataSource.saveUser(any()) } returns Unit

        repository.save(user)

        coVerify { dataSource.saveUser(match { it.id == user.id && it.email == user.email }) }
    }

    @Test
    fun `findByUser should return user when found`() = runTest {
        coEvery { dataSource.findByUser("test@example.com", "password") } returns userRemote

        val result = repository.findByUser("test@example.com", "password").first()

        assertEquals(user.id, result?.id)
        assertEquals(user.email, result?.email)
    }

    @Test
    fun `findByUser should return null when not found`() = runTest {
        coEvery { dataSource.findByUser("test@example.com", "password") } returns null

        val result = repository.findByUser("test@example.com", "password").first()

        assertNull(result)
    }

    @Test
    fun `findByEmail should return user when found`() = runTest {
        coEvery { dataSource.findByEmail("test@example.com") } returns userRemote

        val result = repository.findByEmail("test@example.com").first()

        assertEquals(user.id, result?.id)
        assertEquals(user.email, result?.email)
    }

    @Test
    fun `findByEmail should return null when not found`() = runTest {
        coEvery { dataSource.findByEmail("test@example.com") } returns null

        val result = repository.findByEmail("test@example.com").first()

        assertNull(result)
    }

    @Test
    fun `sendCode and getCode should work together`() = runTest {
        val email = "test@example.com"
        repository.sendCode(email)
        val code = repository.getCode()

        assertEquals(email + "123456", code)
    }
}
