package com.diegoferreiracaetano.dlearn.tmdb

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants.TmdbEndpoints
import com.diegoferreiracaetano.dlearn.model.*
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_API_KEY
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter

internal class TmdbClient(
    private val client: HttpClient,
    private val tmdbMapper: TmdbMapper
) : MovieClient {
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

    override suspend fun getPopularMovies(language: String, favorites: List<Int>): List<Video> {
        val genres = getMovieGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.MOVIE_POPULAR, language).results
            .map { it.toVideo(MediaType.MOVIES, genres, favorites.contains(it.id)) }
    }

    override suspend fun getPopularSeries(language: String, favorites: List<Int>): List<Video> {
        val genres = getTvGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.TV_POPULAR, language).results
            .map { it.toVideo(MediaType.SERIES, genres, favorites.contains(it.id)) }
    }

    override suspend fun getTopRatedMovies(language: String, favorites: List<Int>): List<Video> {
        val genres = getMovieGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.MOVIE_TOP_RATED, language).results
            .map { it.toVideo(MediaType.MOVIES, genres, favorites.contains(it.id)) }
    }

    override suspend fun getTopRatedSeries(language: String, favorites: List<Int>): List<Video> {
        val genres = getTvGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.TV_TOP_RATED, language).results
            .map { it.toVideo(MediaType.SERIES, genres, favorites.contains(it.id)) }
    }

    override suspend fun getMovieGenres(language: String): List<TmdbGenre> {
        return get<TmdbGenresResponse>(TmdbEndpoints.MOVIE_GENRES, language).genres
    }

    override suspend fun getTvGenres(language: String): List<TmdbGenre> {
        return get<TmdbGenresResponse>(TmdbEndpoints.TV_GENRES, language).genres
    }

    override suspend fun getMoviesByGenre(genreId: Int, language: String, favorites: List<Int>): List<Video> {
        val genres = getMovieGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.DISCOVER_MOVIE, language, mapOf("with_genres" to genreId)).results
            .map { it.toVideo(MediaType.MOVIES, genres, favorites.contains(it.id)) }
    }

    override suspend fun getTvByGenre(genreId: Int, language: String, favorites: List<Int>): List<Video> {
        val genres = getTvGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.DISCOVER_TV, language, mapOf("with_genres" to genreId)).results
            .map { it.toVideo(MediaType.SERIES, genres, favorites.contains(it.id)) }
    }

    override suspend fun getMovieDetail(movieId: String, language: String): MovieDetailDomainData {
        val response = get<TmdbMovieDetailRemote>(TmdbEndpoints.movieDetail(movieId), language, mapOf("append_to_response" to "credits,videos,watch/providers,external_ids"))
        return tmdbMapper.toMovieDetail(response)
    }

    override suspend fun getTvShowDetail(tvId: String, language: String): MovieDetailDomainData {
        val response = get<TmdbMovieDetailRemote>(TmdbEndpoints.tvDetail(tvId), language, mapOf("append_to_response" to "credits,videos,watch/providers,external_ids"))
        return tmdbMapper.toMovieDetail(response)
    }

    override suspend fun searchMulti(query: String, language: String): List<Video> {
        val mGenres = getMovieGenres(language)
        val tGenres = getTvGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.SEARCH_MULTI, language, mapOf("query" to query)).results
            .map { item ->
                val isMovie = item.title != null
                item.toVideo(if (isMovie) MediaType.MOVIES else MediaType.SERIES, if (isMovie) mGenres else tGenres)
            }
    }
}
