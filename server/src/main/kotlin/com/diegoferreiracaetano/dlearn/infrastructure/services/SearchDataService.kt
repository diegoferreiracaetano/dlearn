package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class SearchDataService(private val tmdbClient: TmdbClient) {
    suspend fun search(query: String, language: String): List<Video> = coroutineScope {
        if (query.isBlank()) return@coroutineScope emptyList<Video>()

        val movieGenres = async { tmdbClient.getMovieGenres(language).genres }
        val tvGenres = async { tmdbClient.getTvGenres(language).genres }

        val searchResponse = tmdbClient.searchMulti(query, language)

        searchResponse.results.map { item ->
            val isMovie = item.title != null
            val mediaType = if (isMovie) MediaType.MOVIE else MediaType.SERIES
            val genres = if (isMovie) movieGenres.await() else tvGenres.await()
            item.toVideo(mediaType, genres)
        }.sortedByDescending { it.rating ?: 0f }
    }
}
