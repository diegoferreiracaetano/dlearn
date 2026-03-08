package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.ProfileDataService

class GetProfileDataUseCase(private val profileDataService: ProfileDataService) {
    fun execute(userId: String): ProfileDomainData {
        return profileDataService.fetchProfileData(userId)
    }
}
