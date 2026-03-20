package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.infrastructure.services.ProfileDataService

class UpdateProfileDataUseCase(private val profileDataService: ProfileDataService) {
    suspend fun execute(userId: String, updates: Map<String, String>): ProfileDomainData {
        return profileDataService.updateProfileData(userId, updates)
    }
}
