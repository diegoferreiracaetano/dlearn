package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.video.Video
import com.diegoferreiracaetano.dlearn.infrastructure.services.SearchDataService

class GetSearchDataUseCase(private val searchDataService: SearchDataService) {
    suspend fun execute(query: String): List<Video> {
        return searchDataService.search(query)
    }
}
