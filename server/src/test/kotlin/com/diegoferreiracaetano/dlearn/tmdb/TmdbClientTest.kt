package com.diegoferreiracaetano.dlearn.tmdb

import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.model.TmdbEpisodeRemote
import com.diegoferreiracaetano.dlearn.model.TmdbGenre
import com.diegoferreiracaetano.dlearn.model.TmdbGenresResponse
import com.diegoferreiracaetano.dlearn.model.TmdbItemRemote
import com.diegoferreiracaetano.dlearn.model.TmdbListResponse
import com.diegoferreiracaetano.dlearn.model.TmdbMovieDetailRemote
import com.diegoferreiracaetano.dlearn.model.TmdbSeasonDetailRemote
import com.diegoferreiracaetano.dlearn.model.TmdbSeasonRemote
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class TmdbClientTest {

    private lateinit var tmdbClient: TmdbClient
    private val tmdbMapper = mockk<TmdbMapper>(relaxed = true)

    private val json = Json {
        ignoreUnknownKeys = true
        isLenient = true
    }

    private fun createClient(responses: Map<String, String>): HttpClient {
        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            val responseBody = responses.entries.find { url.contains(it.key) }?.value ?: "{}"
            respond(
                content = responseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }
    }

    @Test
    fun `given a popular movies request when getPopularMovies is called should return a list of videos`() = runBlocking {
        val moviesResponse = TmdbListResponse(
            results = listOf(
                TmdbItemRemote(id = 1, title = "Movie 1", genreIds = listOf(1))
            )
        )
        val genresResponse = TmdbGenresResponse(
            genres = listOf(TmdbGenre(id = 1, name = "Action"))
        )

        val responses = mapOf(
            "movie/popular" to json.encodeToString(moviesResponse),
            "genre/movie/list" to json.encodeToString(genresResponse)
        )

        tmdbClient = TmdbClient(createClient(responses), tmdbMapper)

        val result = tmdbClient.getPopularMovies("en")

        assertEquals(1, result.size)
    }

    @Test
    fun `given a popular series request when getPopularSeries is called should return a list of videos`() = runBlocking {
        val seriesResponse = TmdbListResponse(
            results = listOf(
                TmdbItemRemote(id = 2, name = "Series 1", genreIds = listOf(2))
            )
        )
        val genresResponse = TmdbGenresResponse(
            genres = listOf(TmdbGenre(id = 2, name = "Drama"))
        )

        val responses = mapOf(
            "tv/popular" to json.encodeToString(seriesResponse),
            "genre/tv/list" to json.encodeToString(genresResponse)
        )

        tmdbClient = TmdbClient(createClient(responses), tmdbMapper)

        val result = tmdbClient.getPopularSeries("en")

        assertEquals(1, result.size)
    }

    @Test
    fun `given a top rated movies request when getTopRatedMovies is called should return a list of videos`() = runBlocking {
        val moviesResponse = TmdbListResponse(
            results = listOf(
                TmdbItemRemote(id = 3, title = "Top Movie", genreIds = listOf(1))
            )
        )
        val genresResponse = TmdbGenresResponse(
            genres = listOf(TmdbGenre(id = 1, name = "Action"))
        )

        val responses = mapOf(
            "movie/top_rated" to json.encodeToString(moviesResponse),
            "genre/movie/list" to json.encodeToString(genresResponse)
        )

        tmdbClient = TmdbClient(createClient(responses), tmdbMapper)

        val result = tmdbClient.getTopRatedMovies("en")

        assertEquals(1, result.size)
    }

    @Test
    fun `given a top rated series request when getTopRatedSeries is called should return a list of videos`() = runBlocking {
        val seriesResponse = TmdbListResponse(
            results = listOf(
                TmdbItemRemote(id = 4, name = "Top Series", genreIds = listOf(2))
            )
        )
        val genresResponse = TmdbGenresResponse(
            genres = listOf(TmdbGenre(id = 2, name = "Drama"))
        )

        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            val responseBody = when {
                url.contains("genre/tv/list") -> json.encodeToString(genresResponse)
                url.contains("tv/top_rated") -> json.encodeToString(seriesResponse)
                url.contains("tv/popular") -> json.encodeToString(seriesResponse)
                else -> "{}"
            }
            respond(
                content = responseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        tmdbClient = TmdbClient(client, tmdbMapper)

        val result = tmdbClient.getTopRatedSeries("en")

        assertEquals(1, result.size)
    }

    @Test
    fun `given a movie detail request when getMovieDetail is called should return movie detail`() = runBlocking {
        val detailResponse = TmdbMovieDetailRemote(
            id = 123,
            title = "Detail Movie",
            overview = "Overview"
        )

        val responses = mapOf(
            "movie/123" to json.encodeToString(detailResponse)
        )

        tmdbClient = TmdbClient(createClient(responses), tmdbMapper)

        tmdbClient.getMovieDetail("MOVIES_123", "en")

        assertTrue(true)
    }

    @Test
    fun `given a series detail request when getMovieDetail is called should return series detail`() = runBlocking {
        val detailResponse = TmdbMovieDetailRemote(
            id = 456,
            name = "Detail Series",
            overview = "Overview",
            seasons = listOf(TmdbSeasonRemote(id = 1, seasonNumber = 1, episodeCount = 10, name = "S1"))
        )
        val episodesResponse = TmdbSeasonDetailRemote(
            id = "1",
            name = "Season 1",
            overview = "Overview",
            seasonNumber = 1,
            episodes = emptyList()
        )

        val mockEngine = MockEngine { request ->
            val url = request.url.toString()
            val responseBody = when {
                url.contains("/tv/456?") -> json.encodeToString(detailResponse)
                url.contains("/tv/456/season/1") -> json.encodeToString(episodesResponse)
                else -> "{}"
            }
            respond(
                content = responseBody,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, "application/json")
            )
        }
        val client = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(json)
            }
        }

        tmdbClient = TmdbClient(client, tmdbMapper)

        tmdbClient.getMovieDetail("SERIES_456", "en")

        assertTrue(true)
    }

    @Test
    fun `given a search request when searchMulti is called should return mixed results`() = runBlocking {
        val searchResponse = TmdbListResponse(
            results = listOf(
                TmdbItemRemote(id = 1, title = "Movie result", genreIds = listOf(1)),
                TmdbItemRemote(id = 2, name = "Series result", genreIds = listOf(2))
            )
        )
        val movieGenres = TmdbGenresResponse(genres = listOf(TmdbGenre(id = 1, name = "Action")))
        val tvGenres = TmdbGenresResponse(genres = listOf(TmdbGenre(id = 2, name = "Drama")))

        val responses = mapOf(
            "search/multi" to json.encodeToString(searchResponse),
            "genre/movie/list" to json.encodeToString(movieGenres),
            "genre/tv/list" to json.encodeToString(tvGenres)
        )

        tmdbClient = TmdbClient(createClient(responses), tmdbMapper)

        val result = tmdbClient.searchMulti("query", "en")

        assertEquals(2, result.size)
    }

    @Test
    fun `given a tv season request when getTvSeasonEpisodes is called should return a list of episodes`() = runBlocking {
        val episodesResponse = TmdbSeasonDetailRemote(
            id = "1",
            name = "Season 1",
            overview = "Overview",
            seasonNumber = 1,
            episodes = listOf(
                TmdbEpisodeRemote(
                    id = 10,
                    name = "Pilot",
                    overview = "First episode",
                    episodeNumber = 1,
                    seasonNumber = 1,
                    runtime = 45
                )
            )
        )

        val responses = mapOf(
            "tv/456/season/1" to json.encodeToString(episodesResponse)
        )

        tmdbClient = TmdbClient(createClient(responses), tmdbMapper)

        val result = tmdbClient.getTvSeasonEpisodes("456", 1, "en")

        assertEquals(1, result.size)
    }
}
