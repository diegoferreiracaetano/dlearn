package com.diegoferreiracaetano.dlearn.data.auth.challenge.remote

import com.diegoferreiracaetano.dlearn.data.auth.challenge.OtpChallengeHandler
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCoordinator
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class ChallengeRepositoryRemoteTest {

    private val coordinator: ChallengeCoordinator = mockk()
    private val otpHandler: OtpChallengeHandler = mockk(relaxed = true)
    private val json = Json { ignoreUnknownKeys = true }

    private val challenge = Challenge(challengeType = ChallengeType.OTP_EMAIL)
    private val session = ChallengeSession(transactionId = "tx123", challenge = challenge)

    @Before
    fun setup() {
        every { coordinator.currentSession } returns session
        every { coordinator.activeChallenge } returns challenge
        every { coordinator.clear() } returns Unit
    }

    private fun createClient(responseBody: String, status: HttpStatusCode = HttpStatusCode.OK): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = responseBody,
                status = status,
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
    fun `resolveChallenge should return Success when server returns OK`() = runTest {
        val backendResponse = """{"success":true, "validatedToken":"token123"}"""
        val httpClient = createClient(backendResponse)
        val repository = ChallengeRepositoryRemote(httpClient, coordinator, otpHandler)

        val result = repository.resolveChallenge("123456").first()

        assertTrue(result is ChallengeResult.Success)
        assertEquals("token123", (result as ChallengeResult.Success).data["validatedToken"])
        verify { otpHandler.onChallengeResolved("token123") }
        verify { coordinator.clear() }
    }

    @Test
    fun `resolveChallenge should return Failure when server returns error`() = runTest {
        val httpClient = createClient("Error", status = HttpStatusCode.BadRequest)
        val repository = ChallengeRepositoryRemote(httpClient, coordinator, otpHandler)

        val result = repository.resolveChallenge("123456").first()

        assertTrue(result is ChallengeResult.Failure)
    }

    @Test
    fun `resendChallenge should return true when server returns OK`() = runTest {
        val httpClient = createClient("""{"success":true}""")
        val repository = ChallengeRepositoryRemote(httpClient, coordinator, otpHandler)

        val result = repository.resendChallenge().first()

        assertTrue(result)
    }

    @Test
    fun `cancelChallenge should call handler and coordinator clear`() {
        val httpClient = createClient("")
        val repository = ChallengeRepositoryRemote(httpClient, coordinator, otpHandler)

        repository.cancelChallenge()

        verify { otpHandler.onChallengeCancelled() }
        verify { coordinator.clear() }
    }
}
