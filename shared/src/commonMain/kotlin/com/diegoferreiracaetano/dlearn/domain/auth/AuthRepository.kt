package com.diegoferreiracaetano.dlearn.domain.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(
        email: String,
        password: String,
    ): Flow<AuthResponse>

    fun register(
        name: String,
        email: String,
        password: String,
    ): Flow<AuthResponse>

    fun socialLogin(
        provider: String,
        idToken: String,
        accessToken: String? = null,
    ): Flow<AuthResponse>

    suspend fun refreshToken(refreshToken: String): AuthResponse

    suspend fun logout()
}
