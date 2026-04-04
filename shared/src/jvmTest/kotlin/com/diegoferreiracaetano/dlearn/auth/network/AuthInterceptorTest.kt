package com.diegoferreiracaetano.dlearn.auth.network

import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.user.User
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.header
import io.ktor.client.request.request
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class AuthInterceptorTest {

    private val sessionManager = mockk<SessionManager>(relaxed = true)

    @Test
    fun `when intercept is called with REQUIRED mode should add authorization header`() = runTest {
        val token = "secret_token"
        coEvery { sessionManager.token() } returns token
        
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { respond("") }
            }
        }
        
        val interceptor = AuthInterceptor(sessionManager, client)
        val request = HttpRequestBuilder().apply {
            attributes.put(AuthModeKey, AuthMode.REQUIRED)
        }

        interceptor.intercept(request)

        assertEquals("Bearer $token", request.headers[HttpHeaders.Authorization])
    }

    @Test
    fun `when intercept is called with NONE mode should NOT add authorization header`() = runTest {
        val client = HttpClient(MockEngine) {
            engine {
                addHandler { respond("") }
            }
        }
        
        val interceptor = AuthInterceptor(sessionManager, client)
        val request = HttpRequestBuilder().apply {
            attributes.put(AuthModeKey, AuthMode.NONE)
        }

        interceptor.intercept(request)

        assertFalse(request.headers.contains(HttpHeaders.Authorization))
    }

    @Test
    fun `when handleUnauthorized is called and refresh succeeds should update session and return true`() = runTest {
        val oldToken = "old_token"
        val refreshToken = "refresh_token"
        val newUser = User(id = "1", email = "test@test.com", name = "Test")
        val newAuthResponse = AuthResponse(user = newUser, accessToken = "new_token", refreshToken = "new_refresh")
        
        coEvery { sessionManager.token() } returns oldToken
        coEvery { sessionManager.refreshToken() } returns refreshToken
        
        val engine = MockEngine { request ->
            if (request.url.encodedPath == "/v1/auth/refresh") {
                respond(
                    content = Json.encodeToString(newAuthResponse),
                    status = HttpStatusCode.OK,
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            } else {
                respond("", status = HttpStatusCode.Unauthorized)
            }
        }
        
        val client = HttpClient(engine) {
            install(ContentNegotiation) { json() }
        }
        
        val interceptor = AuthInterceptor(sessionManager, client)
        
        val response = client.request {
            header(HttpHeaders.Authorization, "Bearer $oldToken")
        }

        val result = interceptor.handleUnauthorized(response)

        assertTrue(result)
        coVerify { sessionManager.login(newUser, "new_token", "new_refresh") }
    }

    @Test
    fun `when handleUnauthorized is called and current token already changed should return true`() = runTest {
        val oldToken = "old_token"
        val newToken = "new_token"
        coEvery { sessionManager.token() } returns newToken
        
        val engine = MockEngine { respond("", status = HttpStatusCode.Unauthorized) }
        val client = HttpClient(engine)
        val interceptor = AuthInterceptor(sessionManager, client)
        
        val response = client.request {
            header(HttpHeaders.Authorization, "Bearer $oldToken")
        }

        val result = interceptor.handleUnauthorized(response)

        assertTrue(result)
    }

    @Test
    fun `when handleUnauthorized is called and refresh fails should logout and return false`() = runTest {
        val oldToken = "old_token"
        coEvery { sessionManager.token() } returns oldToken
        coEvery { sessionManager.refreshToken() } returns "refresh_token"
        
        val engine = MockEngine { 
            respond("", status = HttpStatusCode.Unauthorized)
        }
        
        val client = HttpClient(engine) {
            install(ContentNegotiation) { json() }
        }
        
        val interceptor = AuthInterceptor(sessionManager, client)
        
        val response = client.request {
            header(HttpHeaders.Authorization, "Bearer $oldToken")
        }

        val result = interceptor.handleUnauthorized(response)

        assertFalse(result)
        coVerify { sessionManager.logout() }
    }

    @Test
    fun `when handleUnauthorized is called with NONE mode should return false`() = runTest {
        val engine = MockEngine { respond("", status = HttpStatusCode.Unauthorized) }
        val client = HttpClient(engine)
        val interceptor = AuthInterceptor(sessionManager, client)
        
        val response = client.request {
            attributes.put(AuthModeKey, AuthMode.NONE)
        }

        val result = interceptor.handleUnauthorized(response)

        assertFalse(result)
    }

    @Test
    fun `when handleUnauthorized is called with non-unauthorized status should return false`() = runTest {
        val engine = MockEngine { respond("", status = HttpStatusCode.BadRequest) }
        val client = HttpClient(engine)
        val interceptor = AuthInterceptor(sessionManager, client)
        
        val response = client.request { }

        val result = interceptor.handleUnauthorized(response)

        assertFalse(result)
    }
}
