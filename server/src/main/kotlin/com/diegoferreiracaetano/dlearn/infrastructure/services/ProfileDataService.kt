package com.diegoferreiracaetano.dlearn.infrastructure.services

import com.diegoferreiracaetano.dlearn.domain.models.ProfileDomainData
import kotlinx.serialization.json.Json
import java.io.InputStream

class ProfileDataService {
    fun fetchProfileData(userId: String): ProfileDomainData {
        val inputStream: InputStream = this.javaClass.classLoader.getResourceAsStream("profile.json")
            ?: throw IllegalStateException("profile.json not found")
        val jsonString = inputStream.bufferedReader().use { it.readText() }
        return Json.decodeFromString<ProfileDomainData>(jsonString)
    }
}
