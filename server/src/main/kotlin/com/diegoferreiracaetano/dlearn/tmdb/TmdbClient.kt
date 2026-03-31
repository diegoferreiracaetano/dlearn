package com.diegoferreiracaetano.dlearn.tmdb

import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants.TmdbEndpoints
import com.diegoferreiracaetano.dlearn.model.TmdbGenresResponse
import com.diegoferreiracaetano.dlearn.model.TmdbItemRemote
import com.diegoferreiracaetano.dlearn.model.TmdbListResponse
import com.diegoferreiracaetano.dlearn.model.TmdbMovieDetailRemote
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_API_KEY
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

class TmdbClient(private val client: HttpClient) {
    private val apiKey = THE_MOVIE_DB_API_KEY
    private val baseUrl = THE_MOVIE_DB_BASE_URL

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
        return get(TmdbEndpoints.MOVIE_POPULAR, language)
    }

    suspend fun getPopularSeries(language: String): TmdbListResponse<TmdbItemRemote> {
        return get(TmdbEndpoints.TV_POPULAR, language)
    }

    suspend fun getTopRatedMovies(language: String): TmdbListResponse<TmdbItemRemote> {
        return get(TmdbEndpoints.MOVIE_TOP_RATED, language)
    }

    suspend fun getTopRatedSeries(language: String): TmdbListResponse<TmdbItemRemote> {
        return get(TmdbEndpoints.TV_TOP_RATED, language)
    }

    suspend fun getMovieGenres(language: String): TmdbGenresResponse {
        return get(TmdbEndpoints.MOVIE_GENRES, language)
    }

    suspend fun getTvGenres(language: String): TmdbGenresResponse {
        return get(TmdbEndpoints.TV_GENRES, language)
    }

    suspend fun getMoviesByGenre(genreId: Int, language: String): TmdbListResponse<TmdbItemRemote> {
        return get(TmdbEndpoints.DISCOVER_MOVIE, language, mapOf("with_genres" to genreId))
    }

    suspend fun getTvByGenre(genreId: Int, language: String): TmdbListResponse<TmdbItemRemote> {
        return get(TmdbEndpoints.DISCOVER_TV, language, mapOf("with_genres" to genreId))
    }

    suspend fun getMovieDetail(movieId: String, language: String): TmdbMovieDetailRemote {
        return get(TmdbEndpoints.movieDetail(movieId), language, mapOf("append_to_response" to "credits,videos,watch/providers,external_ids"))
    }

    suspend fun getTvShowDetail(tvId: String, language: String): TmdbMovieDetailRemote {
        return get(TmdbEndpoints.tvDetail(tvId), language, mapOf("append_to_response" to "credits,videos,watch/providers,external_ids"))
    }

    suspend fun searchMulti(query: String, language: String): TmdbListResponse<TmdbItemRemote> {
        return get(TmdbEndpoints.SEARCH_MULTI, language, mapOf("query" to query))
    }
}
