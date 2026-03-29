package com.diegoferreiracaetano.dlearn.domain.repository

import com.diegoferreiracaetano.dlearn.domain.user.User

interface UserRepository {
    suspend fun findByEmail(email: String): User?
    suspend fun findById(id: String): User?
    suspend fun findAll(): List<User>
    suspend fun save(user: User, password: String): User
    suspend fun update(user: User, password: String? = null): User
    suspend fun authenticate(email: String, password: String): User?
}
