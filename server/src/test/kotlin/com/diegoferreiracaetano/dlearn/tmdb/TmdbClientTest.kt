package com.diegoferreiracaetano.dlearn.tmdb

import io.ktor.client.*
import io.ktor.client.engine.mock.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TmdbClientTest {

    private fun createMockClient(handler: suspend MockRequestHandleScope.(MockHttpRequestData) -> HttpResponseData): HttpClient {
        return HttpClient(MockEngine) {
            engine {
                addHandler(handler)
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    isLenient = true
                })
            }
        }
    }

    @Test
    fun `getPopularMovies deve chamar o endpoint correto`() = runTest {
        val mockClient = createMockClient { request ->
            assertTrue(request.url.encodedPath.endsWith("/movie/popular"))
            assertEquals("pt-BR", request.url.parameters["language"])
            
            respond(
                content = """{"page":1, "results":[], "total_pages":1, "total_results":0}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = TmdbClient(mockClient)
        val response = client.getPopularMovies("pt-BR")
        
        assertTrue(response.results.isEmpty())
    }

    @Test
    fun `getMovieDetail deve incluir parametros de append_to_response`() = runTest {
        val mockClient = createMockClient { request ->
            assertTrue(request.url.encodedPath.contains("/movie/123"))
            assertEquals("credits,videos,watch/providers,external_ids", request.url.parameters["append_to_response"])
            
            respond(
                content = """{"id":123, "title":"Mock Movie"}""",
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val client = TmdbClient(mockClient)
        val response = client.getMovieDetail("123", "en-US")
        
        assertEquals(123, response.id)
    }
}
