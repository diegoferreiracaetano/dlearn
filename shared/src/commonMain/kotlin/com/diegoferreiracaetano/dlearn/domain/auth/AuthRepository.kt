package com.diegoferreiracaetano.dlearn.domain.auth

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun login(email: String, password: String): Flow<AuthResponse>
    suspend fun refreshToken(refreshToken: String): AuthResponse
    suspend fun logout()
}