package com.diegoferreiracaetano.dlearn.domain.auth

import com.diegoferreiracaetano.dlearn.domain.user.MovieProvider
import com.diegoferreiracaetano.dlearn.domain.user.User

interface AccountProvider {
    suspend fun saveAccount(user: User, provider: MovieProvider?, accessToken: String, refreshToken: String)
    suspend fun getAccessToken(): String?
    suspend fun getRefreshToken(): String?
    suspend fun getUser(): User?
    suspend fun getProvider(): MovieProvider?
    suspend fun clearAccount()
    suspend fun hasAccount(): Boolean
}
