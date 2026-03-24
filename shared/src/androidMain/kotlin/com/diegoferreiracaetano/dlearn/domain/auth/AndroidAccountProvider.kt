package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.data.source.local.KeyValueStorage
import com.diegoferreiracaetano.dlearn.domain.user.User
import com.diegoferreiracaetano.dlearn.util.fromJson
import com.diegoferreiracaetano.dlearn.util.toJson

/**
 * Implementação do AccountProvider para Android usando KeyValueStorage (SharedPreferences).
 * Substituiu o AccountManager para evitar SecurityException e facilitar KMP.
 */
class AndroidAccountProvider(
    private val storage: KeyValueStorage
) : AccountProvider {

    private val KEY_ACCESS_TOKEN = "access_token"
    private val KEY_REFRESH_TOKEN = "refresh_token"
    private val KEY_USER_DATA = "user_data"

    override suspend fun saveAccount(user: User, accessToken: String, refreshToken: String) {
        storage.put(KEY_ACCESS_TOKEN, accessToken)
        storage.put(KEY_REFRESH_TOKEN, refreshToken)
        storage.put(KEY_USER_DATA, user.toJson())
    }

    override suspend fun getAccessToken(): String {
        return storage.get(KEY_ACCESS_TOKEN, "")
    }

    override suspend fun getRefreshToken(): String {
        return storage.get(KEY_REFRESH_TOKEN, "")
    }

    override suspend fun getUser(): User {
        return storage.get(KEY_USER_DATA, "").fromJson<User>()
    }

    override suspend fun clearAccount() {
        storage.remove(KEY_ACCESS_TOKEN)
        storage.remove(KEY_REFRESH_TOKEN)
        storage.remove(KEY_USER_DATA)
    }

    override suspend fun hasAccount(): Boolean {
        return storage.get(KEY_ACCESS_TOKEN, "").isNotEmpty()

    }
}