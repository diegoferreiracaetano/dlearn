package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.MovieDetailDataService

class GetMovieDetailUseCase(private val movieDetailDataService: MovieDetailDataService) {
    suspend fun execute(
        movieId: String, 
        language: String,
        userId: String
    ): MovieDetailDomainData {
        return movieDetailDataService.fetchMovieDetail(movieId, language, userId)
    }
}
