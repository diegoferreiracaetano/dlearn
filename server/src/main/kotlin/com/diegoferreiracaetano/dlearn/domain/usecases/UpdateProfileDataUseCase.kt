package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.infrastructure.services.ProfileDataService

class UpdateProfileDataUseCase(private val profileDataService: ProfileDataService) {
    fun execute(userId: String, updates: Map<String, String>) {
        profileDataService.updateProfileData(userId, updates)
    }
}
