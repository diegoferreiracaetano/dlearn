package com.diegoferreiracaetano.dlearn.tmdb

import com.diegoferreiracaetano.dlearn.model.*
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_API_KEY
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.cio.CIO
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.*
import io.ktor.http.ContentType
import io.ktor.http.contentType
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

    private suspend inline fun <reified T> post(
        path: String,
        body: Any,
        params: Map<String, Any> = emptyMap()
    ): T {
        return client.post("$baseUrl$path") {
            parameter("api_key", apiKey)
            params.forEach { (key, value) ->
                parameter(key, value)
            }
            contentType(ContentType.Application.Json)
            setBody(body)
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

    suspend fun getAccountStates(movieId: String, sessionId: String): TmdbAccountStatesRemote {
        return get("/movie/$movieId/account_states", "en", mapOf("session_id" to sessionId))
    }

    suspend fun markAsFavorite(
        accountId: String,
        sessionId: String,
        mediaType: String,
        mediaId: Int,
        favorite: Boolean
    ): TmdbStatusResponse {
        val requestBody = TmdbFavoriteRequest(
            mediaType = mediaType,
            mediaId = mediaId,
            favorite = favorite
        )
        return post("/account/$accountId/favorite", requestBody, mapOf("session_id" to sessionId))
    }

    suspend fun addToWatchlist(
        accountId: String,
        sessionId: String,
        mediaType: String,
        mediaId: Int,
        watchlist: Boolean
    ): TmdbStatusResponse {
        val requestBody = TmdbWatchlistRequest(
            mediaType = mediaType,
            mediaId = mediaId,
            watchlist = watchlist
        )
        return post("/account/$accountId/watchlist", requestBody, mapOf("session_id" to sessionId))
    }

    suspend fun getFavorite(accountId: String, sessionId: String, mediaType: String, language: String): TmdbListResponse<TmdbItemRemote> {
        val type = if (mediaType.lowercase() == "movie") "movies" else "tv"
        return get("/account/$accountId/favorite/$type", language, mapOf("session_id" to sessionId))
    }

    suspend fun getWatchlist(accountId: String, sessionId: String, mediaType: String, language: String): TmdbListResponse<TmdbItemRemote> {
        val type = if (mediaType.lowercase() == "movie") "movies" else "tv"
        return get("/account/$accountId/watchlist/$type", language, mapOf("session_id" to sessionId))
    }

    // Auth methods

    suspend fun createRequestToken(): TmdbRequestTokenResponse {
        return get("/authentication/token/new", "en")
    }

    suspend fun validateWithLogin(request: TmdbLoginRequest): TmdbRequestTokenResponse {
        return post("/authentication/token/validate_with_login", request)
    }

    suspend fun createSession(requestToken: String): TmdbSessionResponse {
        return post("/authentication/session/new", TmdbSessionRequest(requestToken))
    }

    suspend fun getAccountDetails(sessionId: String): TmdbAccountResponse {
        return get("/account", "en", mapOf("session_id" to sessionId))
    }
}
