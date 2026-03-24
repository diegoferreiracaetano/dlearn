package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.ProfileDataService

class UpdateProfileDataUseCase(private val profileDataService: ProfileDataService) {
    suspend fun execute(updates: Map<String, String>): User {
        return profileDataService.updateProfileData( updates)
    }
}
