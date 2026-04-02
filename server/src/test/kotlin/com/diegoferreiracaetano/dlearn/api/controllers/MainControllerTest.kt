package com.diegoferreiracaetano.dlearn.api.controllers

import com.diegoferreiracaetano.dlearn.domain.repository.UserRepository
import com.diegoferreiracaetano.dlearn.infrastructure.services.ChallengeDataService
import com.diegoferreiracaetano.dlearn.orchestrator.app.MainOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.app.Orchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.CreateUserOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.LoginOrchestrator
import com.diegoferreiracaetano.dlearn.orchestrator.auth.PasswordOrchestrator
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
import com.diegoferreiracaetano.dlearn.util.I18nProvider
import io.ktor.client.request.get
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.install
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.AuthenticationContext
import io.ktor.server.auth.AuthenticationProvider
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.routing.routing
import io.ktor.server.testing.testApplication
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import org.junit.Test
import org.koin.dsl.module
import org.koin.ktor.plugin.Koin
import kotlin.test.assertEquals

class MainControllerTest {

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

    @Test
    fun `given an authenticated user when GET v1 main is called should return the screen from orchestrator`() = testApplication {
        val testModule = module {
            single { mainOrchestrator }
            single { loginOrchestrator }
            single { createUserOrchestrator }
            single { passwordOrchestrator }
            single { appOrchestrator }
            single { challengeDataService }
            single { userRepository }
            single { i18nProvider }
        }

        application {
            install(Koin) {
                modules(testModule)
            }
            install(ContentNegotiation) {
                json()
            }
            install(Authentication) {
                register(TestAuthenticationProvider(TestAuthenticationProvider.Config("auth-jwt")))
            }
            routing {
                authenticate("auth-jwt") {
                    mainController()
                }
            }
        }

        val expectedScreen = Screen(components = emptyList())
        every { mainOrchestrator.execute(any(), any(), any()) } returns flowOf(expectedScreen)

        val response = client.get("/v1/main") {
            header(HttpHeaders.Authorization, "Bearer token")
        }

        assertEquals(HttpStatusCode.OK, response.status)
    }
}
