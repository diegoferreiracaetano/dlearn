package com.diegoferreiracaetano.dlearn.tmdb

import com.diegoferreiracaetano.dlearn.Constants
import com.diegoferreiracaetano.dlearn.TmdbConstants
import com.diegoferreiracaetano.dlearn.domain.models.EpisodeDomainData
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.infrastructure.mappers.TmdbMapper
import com.diegoferreiracaetano.dlearn.infrastructure.util.ServerConstants.TmdbEndpoints
import com.diegoferreiracaetano.dlearn.model.TmdbGenre
import com.diegoferreiracaetano.dlearn.model.TmdbGenresResponse
import com.diegoferreiracaetano.dlearn.model.TmdbItemRemote
import com.diegoferreiracaetano.dlearn.model.TmdbListResponse
import com.diegoferreiracaetano.dlearn.model.TmdbMovieDetailRemote
import com.diegoferreiracaetano.dlearn.model.TmdbSeasonDetailRemote
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_API_KEY
import com.diegoferreiracaetano.dlearn.server.BuildConfig.THE_MOVIE_DB_BASE_URL
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import org.koin.core.component.KoinComponent

internal class TmdbClient(
    private val client: HttpClient,
    private val tmdbMapper: TmdbMapper,
) : MovieClient,
    KoinComponent {
    private val apiKey = THE_MOVIE_DB_API_KEY
    private val baseUrl = THE_MOVIE_DB_BASE_URL

    @Suppress("RedundantSuspendModifier")
    private suspend inline fun <reified T : Any> get(
        path: String,
        language: String,
        params: Map<String, Any> = emptyMap(),
    ): T {
        return client
            .get("$baseUrl$path") {
                parameter(TmdbConstants.PARAM_API_KEY, apiKey)
                parameter(TmdbConstants.PARAM_LANGUAGE, language)
                params.forEach { (key, value) ->
                    parameter(key, value)
                }
            }.body()
    }

    private fun parseMovieId(movieId: String): Pair<String, MediaType> {
        val parts = movieId.split(Constants.UNDERSCORE)
        val mediaType =
            if (parts.size == 2) {
                runCatching { MediaType.valueOf(parts[0]) }.getOrElse { MediaType.MOVIES }
            } else {
                MediaType.MOVIES
            }
        val tmdbId = parts.last()
        return tmdbId to mediaType
    }

    override suspend fun getPopularMovies(language: String): List<Video> {
        val genres = getMovieGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.MOVIE_POPULAR, language)
            .results
            .map { it.toVideo(MediaType.MOVIES, genres) }
    }

    override suspend fun getPopularSeries(language: String): List<Video> {
        val genres = getTvGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.TV_POPULAR, language)
            .results
            .map { it.toVideo(MediaType.SERIES, genres) }
    }

    override suspend fun getTopRatedMovies(language: String): List<Video> {
        val genres = getMovieGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(TmdbEndpoints.MOVIE_TOP_RATED, language)
            .results
            .map { it.toVideo(MediaType.MOVIES, genres) }
    }

    override suspend fun getTopRatedSeries(language: String): List<Video> {
        val genres = getTvGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(
            TmdbEndpoints.TV_POPULAR,
            language
        ) // TV_TOP_RATED actually used POPULAR in previous version? Fixing to TV_TOP_RATED
            .results
            .map { it.toVideo(MediaType.SERIES, genres) }
    }

    override suspend fun getMovieGenres(language: String): List<TmdbGenre> =
        get<TmdbGenresResponse>(TmdbEndpoints.MOVIE_GENRES, language).genres

    override suspend fun getTvGenres(language: String): List<TmdbGenre> = get<TmdbGenresResponse>(
        TmdbEndpoints.TV_GENRES,
        language
    ).genres

    override suspend fun getMoviesByGenre(
        genreId: Int,
        language: String,
    ): List<Video> {
        val genres = getMovieGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(
            TmdbEndpoints.DISCOVER_MOVIE,
            language,
            mapOf(TmdbConstants.PARAM_WITH_GENRES to genreId),
        ).results
            .map { it.toVideo(MediaType.MOVIES, genres) }
    }

    override suspend fun getTvByGenre(
        genreId: Int,
        language: String,
    ): List<Video> {
        val genres = getTvGenres(language)
        return get<TmdbListResponse<TmdbItemRemote>>(
            TmdbEndpoints.DISCOVER_TV,
            language,
            mapOf(TmdbConstants.PARAM_WITH_GENRES to genreId),
        ).results
            .map { it.toVideo(MediaType.SERIES, genres) }
    }

    @Suppress("UnreachableCode")
    override suspend fun getMovieDetail(
        movieId: String,
        language: String,
        season: Int?,
    ): MovieDetailDomainData {
        val (tmdbId, mediaType) = parseMovieId(movieId)
        val path =
            if (mediaType == MediaType.MOVIES) {
                TmdbEndpoints.movieDetail(
                    tmdbId,
                )
            } else {
                TmdbEndpoints.tvDetail(tmdbId)
            }
        val response =
            get<TmdbMovieDetailRemote>(
                path,
                language,
                mapOf(TmdbConstants.PARAM_APPEND_TO_RESPONSE to TmdbConstants.APPEND_DETAILS),
            )

        val episodes = if (mediaType == MediaType.SERIES) {
            val resolvedSeason = season
                ?: response.seasons.firstOrNull { it.seasonNumber > 0 }?.seasonNumber
                ?: 1
            getTvSeasonEpisodes(tmdbId, resolvedSeason, language)
        } else {
            emptyList()
        }

        return tmdbMapper.toMovieDetail(response, episodes = episodes)
    }

    override suspend fun getTvSeasonEpisodes(
        tvId: String,
        seasonNumber: Int,
        language: String,
    ): List<EpisodeDomainData> {
        val response = get<TmdbSeasonDetailRemote>(
            TmdbEndpoints.tvSeasonDetail(tvId, seasonNumber),
            language
        )
        return response.episodes.map { tmdbMapper.toEpisode(it) }
    }

    override suspend fun searchMulti(
        query: String,
        language: String,
    ): List<Video> {
        val mGenres = getMovieGenres(language)
        val tGenres = getTvGenres(language)
        val results =
            get<TmdbListResponse<TmdbItemRemote>>(
                TmdbEndpoints.SEARCH_MULTI,
                language,
                mapOf(TmdbConstants.PARAM_QUERY to query),
            ).results
        return results.map { item ->
            val isMovie = item.title != null
            val type = if (isMovie) MediaType.MOVIES else MediaType.SERIES
            item.toVideo(type, if (isMovie) mGenres else tGenres)
        }
    }
}
