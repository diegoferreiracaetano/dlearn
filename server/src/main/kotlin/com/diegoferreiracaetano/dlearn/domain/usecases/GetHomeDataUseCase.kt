package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.home.HomeFilterType
import com.diegoferreiracaetano.dlearn.domain.models.HomeDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.HomeDataService
import com.diegoferreiracaetano.dlearn.network.AppHeader

class GetHomeDataUseCase(private val homeDataService: HomeDataService) {
    suspend fun execute(
        language: String,
        type: HomeFilterType = HomeFilterType.ALL,
        header: AppHeader? = null
    ): HomeDomainData {
        return homeDataService.fetchHomeData(language, type, header)
    }
}
