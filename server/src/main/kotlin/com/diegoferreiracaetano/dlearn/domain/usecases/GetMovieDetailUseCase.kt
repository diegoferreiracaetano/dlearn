package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.MovieDetailDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.MovieDetailDataService
import com.diegoferreiracaetano.dlearn.network.AppHeader

class GetMovieDetailUseCase(private val movieDetailDataService: MovieDetailDataService) {
    suspend fun execute(movieId: String, language: String, header: AppHeader): MovieDetailDomainData {
        return movieDetailDataService.fetchMovieDetail(movieId, language, header)
    }
}
