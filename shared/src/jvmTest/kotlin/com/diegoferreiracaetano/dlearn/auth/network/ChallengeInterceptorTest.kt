package com.diegoferreiracaetano.dlearn.auth.network

import com.diegoferreiracaetano.dlearn.domain.auth.challenge.Challenge
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeCancelledException
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeEngine
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeResult
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeSession
import com.diegoferreiracaetano.dlearn.domain.auth.challenge.ChallengeType
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class ChallengeInterceptorTest {

    private val engine = mockk<ChallengeEngine>()
    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `when response is 428 should trigger challenge resolution and retry with token`() = runTest {
        val transactionId = "tx123"
        val validatedToken = "valid_token"
        val session = ChallengeSession(
            transactionId = transactionId, 
            challenge = Challenge(challengeType = ChallengeType.OTP_SMS)
        )
        
        val mockEngine = MockEngine { request ->
            if (request.headers.contains(SecurityConstants.HEADER_CHALLENGE_TOKEN)) {
                assertEquals(validatedToken, request.headers[SecurityConstants.HEADER_CHALLENGE_TOKEN])
                assertEquals(transactionId, request.headers[SecurityConstants.HEADER_TRANSACTION_ID])
                respond("Success", HttpStatusCode.OK)
            } else {
                respond(
                    content = json.encodeToString(session),
                    status = HttpStatusCode.fromValue(428),
                    headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                )
            }
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
            install(ChallengeInterceptor) {
                this.engine = this@ChallengeInterceptorTest.engine
                this.json = this@ChallengeInterceptorTest.json
            }
        }

        coEvery { engine.resolve(any()) } returns ChallengeResult.Success(mapOf("validatedToken" to validatedToken))

        val response = client.get("/test")
        
        assertEquals(HttpStatusCode.OK, response.status)
    }

    @Test
    fun `when challenge is cancelled should throw ChallengeCancelledException`() = runTest {
        val session = ChallengeSession(
            transactionId = "tx123", 
            challenge = Challenge(challengeType = ChallengeType.OTP_SMS)
        )
        
        val mockEngine = MockEngine { 
            respond(
                content = json.encodeToString(session),
                status = HttpStatusCode.fromValue(428),
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) { json() }
            install(ChallengeInterceptor) {
                this.engine = this@ChallengeInterceptorTest.engine
                this.json = this@ChallengeInterceptorTest.json
            }
        }

        coEvery { engine.resolve(any()) } returns ChallengeResult.Cancelled

        assertFailsWith<ChallengeCancelledException> {
            client.get("/test")
        }
    }
}
