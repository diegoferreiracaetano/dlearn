package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.model.*

interface MovieClient {
    suspend fun getPopularMovies(language: String, favorites: List<Int> = emptyList()): List<Video>
    suspend fun getPopularSeries(language: String, favorites: List<Int> = emptyList()): List<Video>
    suspend fun getTopRatedMovies(language: String, favorites: List<Int> = emptyList()): List<Video>
    suspend fun getTopRatedSeries(language: String, favorites: List<Int> = emptyList()): List<Video>
    suspend fun getMovieGenres(language: String): List<TmdbGenre>
    suspend fun getTvGenres(language: String): List<TmdbGenre>
    suspend fun getMoviesByGenre(genreId: Int, language: String, favorites: List<Int> = emptyList()): List<Video>
    suspend fun getTvByGenre(genreId: Int, language: String, favorites: List<Int> = emptyList()): List<Video>
    suspend fun getMovieDetail(movieId: String, language: String): MovieDetailDomainData
    suspend fun getTvShowDetail(tvId: String, language: String): MovieDetailDomainData
    suspend fun getMovieDetailRemote(movieId: String, language: String): TmdbMovieDetailRemote
    suspend fun getTvShowDetailRemote(tvId: String, language: String): TmdbMovieDetailRemote
    suspend fun searchMulti(query: String, language: String): List<Video>
    suspend fun getAccountStates(id: String, sessionId: String, isGuest: Boolean): TmdbAccountStatesRemote?
    suspend fun getFavorite(accountId: String, sessionId: String, mediaType: MediaType, language: String): TmdbListResponse<TmdbItemRemote>
}
