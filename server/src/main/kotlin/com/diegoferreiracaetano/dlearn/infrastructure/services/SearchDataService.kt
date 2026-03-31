package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.repository.MovieClient
import com.diegoferreiracaetano.dlearn.domain.video.Video

class SearchDataService(private val movieClient: MovieClient) {
    suspend fun search(query: String, language: String): List<Video> {
        if (query.isBlank()) return emptyList()
        return movieClient.searchMulti(query, language).sortedByDescending { it.rating ?: 0f }
    }
}
