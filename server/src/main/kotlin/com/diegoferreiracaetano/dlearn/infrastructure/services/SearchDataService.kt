package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.video.MediaType
import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.model.toVideo
import com.diegoferreiracaetano.dlearn.tmdb.TmdbClient
import kotlinx.coroutines.coroutineScope

class SearchDataService(private val tmdbClient: TmdbClient) {
    suspend fun search(query: String): List<Video> = coroutineScope {
        if (query.isBlank()) return@coroutineScope emptyList<Video>()

        val searchResponse = tmdbClient.searchMulti(query)
        
        searchResponse.results.map { item ->
            val mediaType = if (item.title != null) MediaType.MOVIE else MediaType.SERIES
            item.toVideo(mediaType)
        }.sortedByDescending { it.rating ?: 0f }
    }
}
