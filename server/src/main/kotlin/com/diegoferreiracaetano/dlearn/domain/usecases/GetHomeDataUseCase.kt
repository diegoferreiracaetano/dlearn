package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.HomeDataService

class GetHomeDataUseCase(private val homeDataService: HomeDataService) {
    suspend fun execute(
        userId: String,
        type: HomeFilterType = HomeFilterType.ALL,
        categoryId: String? = null
    ): HomeDomainData {
        return homeDataService.fetchHomeData(userId, type, categoryId)
    }
}
