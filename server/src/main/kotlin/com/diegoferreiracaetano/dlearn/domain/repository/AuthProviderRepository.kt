package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.user.AuthProvider

interface AuthProviderRepository {
    suspend fun findByUserId(userId: String): List<AuthProvider>
    suspend fun saveAll(userId: String, providers: List<AuthProvider>)
    suspend fun deleteByUserId(userId: String)
}
