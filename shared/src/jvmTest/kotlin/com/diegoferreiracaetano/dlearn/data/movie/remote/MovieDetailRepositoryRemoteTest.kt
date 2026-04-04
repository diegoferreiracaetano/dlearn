package com.diegoferreiracaetano.dlearn.data.movie.remote

import com.diegoferreiracaetano.dlearn.ui.sdui.AppRequest
import com.diegoferreiracaetano.dlearn.ui.sdui.Screen
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

class MovieDetailRepositoryRemoteTest {

    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Test
    fun `execute should return Screen from server`() = runTest {
        val expectedScreen = Screen(components = emptyList())
        val mockEngine = MockEngine { request ->
            respond(
                content = json.encodeToString(expectedScreen),
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
        val repository = MovieDetailRepositoryRemote(httpClient)

        val result = repository.execute(AppRequest(path = "/test")).first()

        assertEquals(expectedScreen, result)
    }
}
