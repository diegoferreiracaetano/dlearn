package com.diegoferreiracaetano.dlearn.data.auth.remote

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.user.User
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class AuthRepositoryRemoteTest {

    private val sessionManager: SessionManager = mockk()
    private val json = Json { ignoreUnknownKeys = true }

    private val user = User(id = "1", name = "Test", email = "test@test.com")
    private val authResponse = AuthResponse(user = user, accessToken = "access", refreshToken = "refresh")

    private fun createClient(response: AuthResponse): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = json.encodeToString(response),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    @Test
    fun `login should call sessionManager login on success`() = runTest {
        val httpClient = createClient(authResponse)
        val repository = AuthRepositoryRemote(httpClient, sessionManager)
        coEvery { sessionManager.login(any(), any(), any()) } returns Unit

        val result = repository.login("email", "pass").first()

        assertEquals(authResponse, result)
        coVerify { sessionManager.login(user, "access", "refresh") }
    }

    @Test
    fun `register should call sessionManager logout and then login on success`() = runTest {
        val httpClient = createClient(authResponse)
        val repository = AuthRepositoryRemote(httpClient, sessionManager)
        coEvery { sessionManager.logout() } returns Unit
        coEvery { sessionManager.login(any(), any(), any()) } returns Unit

        val result = repository.register("name", "email", "pass").first()

        assertEquals(authResponse, result)
        coVerify { sessionManager.logout() }
        coVerify { sessionManager.login(user, "access", "refresh") }
    }

    @Test
    fun `socialLogin should call sessionManager login on success`() = runTest {
        val httpClient = createClient(authResponse)
        val repository = AuthRepositoryRemote(httpClient, sessionManager)
        coEvery { sessionManager.login(any(), any(), any()) } returns Unit

        val result = repository.socialLogin("google", "idToken", "accessToken").first()

        assertEquals(authResponse, result)
        coVerify { sessionManager.login(user, "access", "refresh") }
    }

    @Test
    fun `refreshToken should return AuthResponse`() = runTest {
        val httpClient = createClient(authResponse)
        val repository = AuthRepositoryRemote(httpClient, sessionManager)

        val result = repository.refreshToken("refresh")

        assertEquals(authResponse, result)
    }

    @Test
    fun `logout should call httpClient post and sessionManager logout`() = runTest {
        val mockEngine = MockEngine { _ ->
            respond(content = "", status = HttpStatusCode.OK)
        }
        val httpClient = HttpClient(mockEngine)
        val repository = AuthRepositoryRemote(httpClient, sessionManager)
        coEvery { sessionManager.logout() } returns Unit

        repository.logout()

        coVerify { sessionManager.logout() }
    }
}
