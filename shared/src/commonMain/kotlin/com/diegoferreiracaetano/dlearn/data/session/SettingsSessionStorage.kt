package com.diegoferreiracaetano.dlearn.data.session

import com.russhwolf.settings.Settings

class SettingsSessionStorage(
    private val settings: Settings,
) : SessionStorage {
    companion object {
        private const val KEY_AUTH_TOKEN = "auth_token"
        private const val KEY_USER = "user"
    }

    override suspend fun saveToken(token: String) {
        settings.putString(KEY_AUTH_TOKEN, token)
    }

    override fun getToken(): String? = settings.getStringOrNull(KEY_AUTH_TOKEN)

    override fun hasSession(): Boolean = settings.hasKey(KEY_AUTH_TOKEN)

    override suspend fun saveUser(userJson: String) {
        settings.putString(KEY_USER, userJson)
    }

    override fun getUser(): String? = settings.getStringOrNull(KEY_USER)

    override suspend fun clear() {
        settings.remove(KEY_AUTH_TOKEN)
    }
}
