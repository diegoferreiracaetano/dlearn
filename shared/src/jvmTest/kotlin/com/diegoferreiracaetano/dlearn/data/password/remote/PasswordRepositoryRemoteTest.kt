package com.diegoferreiracaetano.dlearn.data.password.remote

import com.diegoferreiracaetano.dlearn.domain.challenge.ChallengeStatus
import com.diegoferreiracaetano.dlearn.domain.password.ChangePasswordResponse
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class PasswordRepositoryRemoteTest {

    private val json = Json { ignoreUnknownKeys = true }

    @Test
    fun `changePassword should return ChangePasswordResponse from server`() = runTest {
        val expectedResponse = ChangePasswordResponse(message = "Success", status = ChallengeStatus.SUCCESS)
        val mockEngine = MockEngine { _ ->
            respond(
                content = json.encodeToString(expectedResponse),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        val repository = PasswordRepositoryRemote(httpClient)

        val result = repository.changePassword("new_password").first()

        assertEquals(expectedResponse, result)
    }
}
