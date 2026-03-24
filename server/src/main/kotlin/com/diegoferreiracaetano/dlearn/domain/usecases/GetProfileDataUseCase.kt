package com.diegoferreiracaetano.dlearn.domain.usecases

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.infrastructure.services.ProfileDataService

class GetProfileDataUseCase(private val profileDataService: ProfileDataService) {
    suspend fun execute(): User {
        return profileDataService.fetchProfileData()
    }
}
