package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.user.User

open class ProfileDataService {
    private val mockUser = User(
        id = "admin",
        name = "Diego Caetano",
        email = "admin@dlearn.com",
        imageUrl = "https://avatars.githubusercontent.com/u/16550604?v=4",
        isPremium = true,
        phoneNumber = "+55 11 99999-9999"
    )

    open suspend fun fetchProfileData(userId: String): User {
        return if (userId == "guest") throw IllegalStateException("Guest user has no profile") 
               else mockUser.copy(id = userId)
    }

    open suspend fun updateProfileData(userId: String, updates: Map<String, String>): User {
        val current = fetchProfileData(userId)
        return current.copy(
            name = updates["full_name"] ?: current.name,
            email = updates["email"] ?: current.email,
            phoneNumber = updates["phone"] ?: current.phoneNumber
        )
    }
}
