package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.auth.network.SecurityConstants
import com.diegoferreiracaetano.dlearn.domain.auth.AuthResponse
import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.ChallengeDataService
import com.diegoferreiracaetano.dlearn.orchestrator.app.MainOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.app.Orchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.CreateUserOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.LoginOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.PasswordOrchestrator
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.client.request.header
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Test
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import kotlin.test.assertEquals

class AuthControllerTest {

    private val mainOrchestrator = mockk<MainOrchestrator>()
    private val loginOrchestrator = mockk<LoginOrchestrator>()
    private val createUserOrchestrator = mockk<CreateUserOrchestrator>()
    private val passwordOrchestrator = mockk<PasswordOrchestrator>()
    private val appOrchestrator = mockk<Orchestrator>()
    private val challengeDataService = mockk<ChallengeDataService>()
    private val userRepository = mockk<UserRepository>()
    private val i18nProvider = mockk<I18nProvider>()

    class TestAuthenticationProvider(config: Config) : AuthenticationProvider(config) {
        override suspend fun onAuthenticate(context: AuthenticationContext) {
            val payload = mockk<com.auth0.jwt.interfaces.Payload>(relaxed = true)
            every { payload.getClaim("user_id").asString() } returns "user123"
            context.principal(JWTPrincipal(payload))
        }

        class Config(name: String?) : AuthenticationProvider.Config(name)
    }

    private fun buildTestModule() = module {
        single { mainOrchestrator }
        single { loginOrchestrator }
        single { createUserOrchestrator }
        single { passwordOrchestrator }
        single { appOrchestrator }
        single { challengeDataService }
        single { userRepository }
        single { i18nProvider }
    }

    @Test
    fun `given a valid request when POST v1 auth login is called should return OK`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            install(Authentication) {
                register(TestAuthenticationProvider(TestAuthenticationProvider.Config("auth-jwt")))
            }
            routing { authController() }
        }

        val expectedResponse = AuthResponse(accessToken = "token", refreshToken = "refresh")
        coEvery { loginOrchestrator.login(any(), any(), any()) } returns expectedResponse

        val response = client.post("/v1/auth/login") {
            contentType(ContentType.Application.Json)
            setBody("""{"email":"test@test.com","password":"pass123"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `given a valid request when POST v1 auth social-login is called should return OK`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            install(Authentication) {
                register(TestAuthenticationProvider(TestAuthenticationProvider.Config("auth-jwt")))
            }
            routing { authController() }
        }

        val expectedResponse = AuthResponse(accessToken = "token", refreshToken = "refresh")
        coEvery { loginOrchestrator.socialLogin(any(), any(), any(), any()) } returns expectedResponse

        val response = client.post("/v1/auth/social-login") {
            contentType(ContentType.Application.Json)
            setBody(
                """{"provider":"google","id_token":"eyJhbGciOiJSUzI1NiJ9.eyJlbWFpbCI6InRlc3RAdGVzdC5jb20iLCJuYW1lIjoiVGVzdCJ9.sig"}"""
            )
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `given a request without provider when POST v1 auth social-login is called should return BadRequest`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            install(Authentication) {
                register(TestAuthenticationProvider(TestAuthenticationProvider.Config("auth-jwt")))
            }
            routing { authController() }
        }

        val response = client.post("/v1/auth/social-login") {
            contentType(ContentType.Application.Json)
            setBody("""{"access_token":"some_token"}""")
        }

        assertEquals(HttpStatusCode.BadRequest, response.status)
    }

    @Test
    fun `given a valid challenge token when POST v1 auth register is called should return Created`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            install(Authentication) {
                register(TestAuthenticationProvider(TestAuthenticationProvider.Config("auth-jwt")))
            }
            routing { authController() }
        }

        val expectedResponse = AuthResponse(accessToken = "token", refreshToken = "refresh")
        every { challengeDataService.isTokenValidated(any()) } returns true
        coEvery { createUserOrchestrator.create(any(), any(), any(), any()) } returns expectedResponse

        val response = client.post("/v1/auth/register") {
            header(SecurityConstants.HEADER_CHALLENGE_TOKEN, "valid-challenge-token")
            contentType(ContentType.Application.Json)
            setBody("""{"name":"Test User","email":"test@test.com","password":"pass123"}""")
        }

        assertEquals(HttpStatusCode.Created, response.status)
    }

    @Test
    fun `given a valid refresh token when POST v1 auth refresh is called should return OK`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            install(Authentication) {
                register(TestAuthenticationProvider(TestAuthenticationProvider.Config("auth-jwt")))
            }
            routing { authController() }
        }

        val expectedResponse = AuthResponse(accessToken = "new-token", refreshToken = "new-refresh")
        coEvery { loginOrchestrator.refreshToken(any(), any()) } returns expectedResponse

        val response = client.post("/v1/auth/refresh") {
            contentType(ContentType.Application.Json)
            setBody("""{"refreshToken":"some-refresh-token"}""")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `given any request when POST v1 auth logout is called should return OK`() = testApplication {
        application {
            install(Koin) { modules(buildTestModule()) }
            install(ContentNegotiation) { json() }
            install(Authentication) {
                register(TestAuthenticationProvider(TestAuthenticationProvider.Config("auth-jwt")))
            }
            routing { authController() }
        }

        val response = client.post("/v1/auth/logout")

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
