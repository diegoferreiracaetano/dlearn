package com.diegoferreiracaetano.dlearn.tmdb

import com.diegoferreiracaetano.dlearn.model.TmdbGenresResponse
import com.diegoferreiracaetano.dlearn.model.TmdbListResponse
import com.diegoferreiracaetano.dlearn.model.TmdbItemRemote
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_API_KEY
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json

internal class TmdbClient {
    private val apiKey = THE_MOVIE_DB_API_KEY
    private val baseUrl = THE_MOVIE_DB_BASE_URL


    private val client = HttpClient(CIO) {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                isLenient = true
            })
        }
    }

    private suspend inline fun <reified T> get(path: String, params: Map<String, Any> = emptyMap()): T {
        return client.get("$baseUrl$path") {
            parameter("api_key", apiKey)
            parameter("language", "pt-BR")
            params.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }

    suspend fun getPopularMovies(): TmdbListResponse<TmdbItemRemote> {
        return get("/movie/popular")
    }

    suspend fun getPopularSeries(): TmdbListResponse<TmdbItemRemote> {
        return get("/tv/popular")
    }

    suspend fun getTopRatedMovies(): TmdbListResponse<TmdbItemRemote> {
        return get("/movie/top_rated")
    }

    suspend fun getMovieGenres(): TmdbGenresResponse {
        return get("/genre/movie/list")
    }

    suspend fun getMoviesByGenre(genreId: Int): TmdbListResponse<TmdbItemRemote> {
        return get("/discover/movie", mapOf("with_genres" to genreId))
    }
}
