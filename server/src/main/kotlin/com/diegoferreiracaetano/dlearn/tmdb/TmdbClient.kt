package com.diegoferreiracaetano.dlearn.tmdb

import com.diegoferreiracaetano.dlearn.model.TmdbGenresResponse
import com.diegoferreiracaetano.dlearn.model.TmdbItemRemote
import com.diegoferreiracaetano.dlearn.model.TmdbListResponse
import com.diegoferreiracaetano.dlearn.model.TmdbMovieDetailRemote
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

class TmdbClient {
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

    private suspend inline fun <reified T> get(
        path: String,
        language: String,
        params: Map<String, Any> = emptyMap()
    ): T {
        return client.get("$baseUrl$path") {
            parameter("api_key", apiKey)
            parameter("language", language)
            params.forEach { (key, value) ->
                parameter(key, value)
            }
        }.body()
    }

    suspend fun getPopularMovies(language: String): TmdbListResponse<TmdbItemRemote> {
        return get("/movie/popular", language)
    }

    suspend fun getPopularSeries(language: String): TmdbListResponse<TmdbItemRemote> {
        return get("/tv/popular", language)
    }

    suspend fun getTopRatedMovies(language: String): TmdbListResponse<TmdbItemRemote> {
        return get("/movie/top_rated", language)
    }

    suspend fun getTopRatedSeries(language: String): TmdbListResponse<TmdbItemRemote> {
        return get("/tv/top_rated", language)
    }

    suspend fun getMovieGenres(language: String): TmdbGenresResponse {
        return get("/genre/movie/list", language)
    }

    suspend fun getTvGenres(language: String): TmdbGenresResponse {
        return get("/genre/tv/list", language)
    }

    suspend fun getMoviesByGenre(genreId: Int, language: String): TmdbListResponse<TmdbItemRemote> {
        return get("/discover/movie", language, mapOf("with_genres" to genreId))
    }

    suspend fun getTvByGenre(genreId: Int, language: String): TmdbListResponse<TmdbItemRemote> {
        return get("/discover/tv", language, mapOf("with_genres" to genreId))
    }

    suspend fun getMovieDetail(movieId: String, language: String): TmdbMovieDetailRemote {
        return get("/movie/$movieId", language, mapOf("append_to_response" to "credits,videos,watch/providers,external_ids"))
    }

    suspend fun getTvShowDetail(tvId: String, language: String): TmdbMovieDetailRemote {
        return get("/tv/$tvId", language, mapOf("append_to_response" to "credits,videos,watch/providers,external_ids"))
    }

    suspend fun searchMulti(query: String, language: String): TmdbListResponse<TmdbItemRemote> {
        return get("/search/multi", language, mapOf("query" to query))
    }
}
