package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import com.diegoferreiracaetano.dlearn.util.fromJson
import java.io.InputStream

open class ProfileDataService {
    private var currentUser: ProfileDomainData? = null

    open fun fetchProfileData(userId: String): ProfileDomainData {
        if (currentUser == null) {
            val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("profile.json")
                ?: throw IllegalStateException("profile.json not found")
            val jsonString = inputStream.bufferedReader().use { it.readText() }
            currentUser = jsonString.fromJson<ProfileDomainData>()
        }
        return currentUser!!
    }

    open fun updateProfileData(userId: String, updates: Map<String, String>): ProfileDomainData {
        val current = fetchProfileData(userId)
        currentUser = current.copy(
            name = updates["full_name"] ?: current.name,
            email = updates["email"] ?: current.email,
            phoneNumber = updates["phone"] ?: current.phoneNumber,
            country = updates["pref_country"] ?: current.country,
            language = updates["pref_language"] ?: current.language
        )

        return currentUser ?: throw IllegalStateException("Profile update failed")
    }
}
