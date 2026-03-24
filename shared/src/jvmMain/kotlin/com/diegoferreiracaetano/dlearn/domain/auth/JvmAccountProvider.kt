package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.util.fromJson
import com.diegoferreiracaetano.dlearn.util.toJson
import com.russhwolf.settings.Settings

class JvmAccountProvider : AccountProvider {
    private val settings = Settings()

    override suspend fun saveAccount(user: User, accessToken: String, refreshToken: String) {
        settings.putString("access_token", accessToken)
        settings.putString("refresh_token", refreshToken)
        settings.putString("user_data", user.toJson())
    }

    override suspend fun getAccessToken(): String? = settings.getStringOrNull("access_token")

    override suspend fun getRefreshToken(): String? = settings.getStringOrNull("refresh_token")

    override suspend fun getUser(): User? = settings.getStringOrNull("user_data")?.fromJson<User>()

    override suspend fun clearAccount() {
        settings.remove("access_token")
        settings.remove("refresh_token")
        settings.remove("user_data")
    }

    override suspend fun hasAccount(): Boolean = settings.hasKey("access_token")
}
