package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.HomeDataService

class GetHomeDataUseCase(private val homeDataService: HomeDataService) {
    suspend fun execute(userId: String): HomeDomainData {
        return homeDataService.fetchHomeData(userId)
    }
}
