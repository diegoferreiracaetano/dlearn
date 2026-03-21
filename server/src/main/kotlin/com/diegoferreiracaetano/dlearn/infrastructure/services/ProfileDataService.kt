package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import kotlinx.serialization.json.Json
import java.io.InputStream

open class ProfileDataService {
    private var currentUser: ProfileDomainData? = null

    open fun fetchProfileData(userId: String): ProfileDomainData {
        if (currentUser == null) {
            val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("profile.json")
                ?: throw IllegalStateException("profile.json not found")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            currentUser = Json.decodeFromString<ProfileDomainData>(jsonString)
        }
        return currentUser!!
    }

    open fun updateProfileData(userId: String, updates: Map<String, String>): ProfileDomainData {
        val current = fetchProfileData(userId)
        currentUser = current.copy(
            name = updates["full_name"] ?: current.name,
            email = updates["email"] ?: current.email,
            phoneNumber = updates["phone"] ?: current.phoneNumber
        )

        return if(currentUser != null)
            currentUser!!
        else
            throw IllegalStateException("profile.json not found")
    }
}
