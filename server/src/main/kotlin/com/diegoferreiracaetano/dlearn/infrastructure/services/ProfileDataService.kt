package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.session.SessionManager
import com.diegoferreiracaetano.dlearn.domain.user.User

open class ProfileDataService(
    private val sessionManager: SessionManager
) {
    open suspend fun fetchProfileData(): User {
        return sessionManager.user()
    }

    open suspend fun updateProfileData(updates: Map<String, String>): User {
        val current = sessionManager.user()
        val updateUser = sessionManager.user().copy(
            name = updates["full_name"] ?: current.name,
            email = updates["email"] ?: current.email,
            phoneNumber = updates["phone"] ?: current.phoneNumber
        )

        return updateUser
    }
}
