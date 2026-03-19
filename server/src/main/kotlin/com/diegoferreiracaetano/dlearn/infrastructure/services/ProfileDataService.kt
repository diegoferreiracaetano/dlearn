package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import kotlinx.serialization.json.Json
import java.io.InputStream

class ProfileDataService {
    private var currentUser: ProfileDomainData? = null

    fun fetchProfileData(userId: String): ProfileDomainData {
        if (currentUser == null) {
            val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("profile.json")
                ?: throw IllegalStateException("profile.json not found")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            currentUser = Json.decodeFromString<ProfileDomainData>(jsonString)
        }
        return currentUser!!
    }

    fun updateProfileData(userId: String, updates: Map<String, String>) {
        val current = fetchProfileData(userId)
        currentUser = current.copy(
            name = updates["full_name"] ?: current.name,
            email = updates["email"] ?: current.email,
            phoneNumber = updates["phone"] ?: current.phoneNumber
            // Password logic can be added here if needed
        )
    }
}
