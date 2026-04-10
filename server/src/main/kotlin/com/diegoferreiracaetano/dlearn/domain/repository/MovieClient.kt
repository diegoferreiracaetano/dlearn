package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.models.EpisodeDomainData
import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.model.TmdbGenre

interface MovieClient {
    suspend fun getPopularMovies(language: String): List<Video>

    suspend fun getPopularSeries(language: String): List<Video>

    suspend fun getTopRatedMovies(language: String): List<Video>

    suspend fun getTopRatedSeries(language: String): List<Video>

    suspend fun getMovieGenres(language: String): List<TmdbGenre>

    suspend fun getTvGenres(language: String): List<TmdbGenre>

    suspend fun getMoviesByGenre(
        genreId: Int,
        language: String,
    ): List<Video>

    suspend fun getTvByGenre(
        genreId: Int,
        language: String,
    ): List<Video>

    suspend fun getMovieDetail(
        movieId: String,
        language: String,
        season: Int? = null,
    ): MovieDetailDomainData

    suspend fun getTvSeasonEpisodes(
        tvId: String,
        seasonNumber: Int,
        language: String,
    ): List<EpisodeDomainData>

    suspend fun searchMulti(
        query: String,
        language: String,
    ): List<Video>
}
