package com.diegoferreiracaetano.dlearn.data.profile.remote

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
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals

class ProfileRepositoryRemoteTest {

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun createClient(response: String): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = response,
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
    fun `when getProfile is called should return screen from api`() = runTest {
        val screen = Screen(components = emptyList())
        val client = createClient(json.encodeToString(Screen.serializer(), screen))
        val repository = ProfileRepositoryRemote(client)

        val result = repository.getProfile("en").first()

        assertEquals(screen, result)
    }

    @Test
    fun `when getProfile is called with null language should still return screen`() = runTest {
        val screen = Screen(components = emptyList())
        val client = createClient(json.encodeToString(Screen.serializer(), screen))
        val repository = ProfileRepositoryRemote(client)

        val result = repository.getProfile(null).first()

        assertEquals(screen, result)
    }
}
