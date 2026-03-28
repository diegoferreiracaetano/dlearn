package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.user.User

interface AccountProvider {
    suspend fun saveAccount(
        user: User, 
        accessToken: String, 
        refreshToken: String
    )
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getUser(): User?
    suspend fun clearAccount()
    suspend fun hasAccount(): Boolean
}
